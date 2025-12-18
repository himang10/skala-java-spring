package com.skala.purejava.http.server;

import java.util.*;
import java.util.regex.*;

/**
 * HTTP 요청을 적절한 핸들러로 라우팅하는 클래스
 *
 * 라우터의 핵심 기능:
 * 1. URL 패턴과 핸들러를 매핑하여 저장
 * 2. 들어온 요청을 등록된 패턴과 매칭
 * 3. 경로 변수 추출 (예: /users/{id}에서 {id} 값 추출)
 *
 * 사용 예시:
 * Router router = new Router();
 *
 * // 라우트 등록
 * router.add("GET", "/users", (req, res) -> { ... });           // 사용자 목록
 * router.add("GET", "/users/{id}", (req, res) -> { ... });      // 특정 사용자
 * router.add("POST", "/users", (req, res) -> { ... });          // 사용자 생성
 * router.add("PUT", "/users/{id}", (req, res) -> { ... });      // 사용자 수정
 * router.add("DELETE", "/users/{id}", (req, res) -> { ... });   // 사용자 삭제
 *
 * // 요청 매칭
 * Optional<Matched> result = router.match("GET", "/users/123");
 * if (result.isPresent()) {
 *     RouteHandler handler = result.get().handler();
 *     Map<String, String> pathVars = result.get().pathVars(); // {id: "123"}
 *     handler.handle(request, response);
 * }
 */
public class Router {

    // 등록된 모든 라우트를 저장하는 리스트
    // 순서대로 매칭을 시도하므로 등록 순서가 중요함
    private final List<Route> routes = new ArrayList<>();

    /**
     * 새로운 라우트를 등록합니다
     *
     * 등록 예시:
     * add("GET", "/users", userListHandler);           // 정적 경로
     * add("GET", "/users/{id}", userDetailHandler);    // 동적 경로 (경로 변수)
     * add("POST", "/api/v1/users/{id}/posts/{postId}", postHandler); // 복잡한 경로
     *
     * @param method HTTP 메서드 (GET, POST, PUT, DELETE 등) - 대소문자 무관
     * @param path   URL 패턴 (/users, /users/{id} 등)
     * @param handler 요청을 처리할 핸들러
     */
    public void add(String method, String path, RouteHandler handler) {
        // HTTP 메서드를 대문자로 통일하여 저장 (GET, get, Get 모두 동일하게 처리)
        routes.add(new Route(method.toUpperCase(), path, handler));
    }

    /**
     * 들어온 HTTP 요청과 매칭되는 라우트를 찾습니다
     *
     * 매칭 과정:
     * 1. 등록된 라우트들을 순서대로 확인
     * 2. HTTP 메서드가 일치하는지 확인
     * 3. URL 패턴이 일치하는지 정규표현식으로 확인
     * 4. 경로 변수가 있다면 값을 추출
     * 5. 첫 번째로 매칭되는 라우트를 반환
     *
     * 매칭 예시:
     * - 요청: "GET /users/123"
     * - 패턴: "/users/{id}"
     * - 결과: 매칭 성공, pathVars = {id: "123"}
     *
     * @param method HTTP 메서드
     * @param path   요청 경로
     * @return 매칭된 라우트와 경로 변수, 매칭 실패시 Optional.empty()
     */
    public Optional<Matched> match(String method, String path) {
        // 등록된 모든 라우트에 대해 순차적으로 매칭 시도
        for (Route route : routes) {
            // 경로 변수를 담을 맵 생성 (매칭이 성공하면 값이 채워짐)
            Map<String, String> pathVars = new HashMap<>();

            // 현재 라우트가 요청과 매칭되는지 확인
            if (route.matches(method.toUpperCase(), path, pathVars)) {
                // 매칭 성공시 핸들러와 경로 변수를 함께 반환
                return Optional.of(new Matched(route.handler, pathVars));
            }
        }
        // 매칭되는 라우트가 없으면 빈 Optional 반환 (404 Not Found가 됨)
        return Optional.empty();
    }

    /**
     * 개별 라우트 정보를 담는 내부 클래스
     *
     * 각 라우트는 다음 정보를 포함:
     * - HTTP 메서드와 URL 패턴
     * - 컴파일된 정규표현식 패턴
     * - 경로 변수 이름들
     * - 요청 처리 핸들러
     */
    private static class Route {
        final String method;              // HTTP 메서드 (GET, POST 등)
        final String pathTemplate;       // 원본 경로 템플릿 (/users/{id})
        final Pattern pathPattern;       // 컴파일된 정규표현식 패턴
        final List<String> paramNames;   // 경로 변수 이름들 [id, postId]
        final RouteHandler handler;       // 요청 처리 핸들러

        /**
         * Route 생성자 - 경로 템플릿을 정규표현식으로 컴파일
         */
        Route(String method, String pathTemplate, RouteHandler handler) {
            this.method = method;
            this.pathTemplate = pathTemplate;
            this.handler = handler;

            // 경로 템플릿을 정규표현식으로 변환
            // 예: "/users/{id}" → "^/users/(?<id>[^/]+)$"
            CompiledPath compiled = compilePath(this.pathTemplate);
            this.pathPattern = compiled.pattern;
            this.paramNames = compiled.paramNames;
        }

        /**
         * 경로 템플릿을 정규표현식으로 컴파일하는 핵심 메서드
         *
         * 변환 과정:
         * 1. 경로를 "/" 기준으로 세그먼트 분리
         * 2. 각 세그먼트가 경로 변수({id})인지 확인
         * 3. 경로 변수는 Named Capturing Group으로 변환
         * 4. 일반 문자열은 Pattern.quote()로 이스케이프 처리
         *
         * 변환 예시:
         * "/users/{id}"           → "^/users/(?<id>[^/]+)$"
         * "/api/v1/posts/{postId}" → "^/api/v1/posts/(?<postId>[^/]+)$"
         * "/users/{id}/posts/{pid}" → "^/users/(?<id>[^/]+)/posts/(?<pid>[^/]+)$"
         *
         * @param template 경로 템플릿 (예: "/users/{id}")
         * @return 컴파일된 패턴과 파라미터 이름들
         */
        private static CompiledPath compilePath(String template) {
            List<String> paramNames = new ArrayList<>();
            StringBuilder regex = new StringBuilder("^");  // 정규표현식 시작

            // 경로를 "/" 기준으로 분리 (예: "/users/{id}" → ["", "users", "{id}"])
            String[] segments = template.split("/");
            for (String segment : segments) {
                if (segment.isEmpty()) continue;  // 빈 세그먼트 건너뛰기

                regex.append("/");  // 세그먼트 구분자 추가

                // 경로 변수인지 확인 (중괄호로 둘러싸인 경우)
                if (segment.startsWith("{") && segment.endsWith("}")) {
                    // 경로 변수 처리
                    String paramName = segment.substring(1, segment.length() - 1);  // {id} → id
                    paramNames.add(paramName);

                    // Named Capturing Group으로 변환
                    // (?<id>[^/]+) : "id"라는 이름의 그룹으로 "/" 이외의 문자들을 캡처
                    regex.append("(?<").append(paramName).append(">[^/]+)");
                } else {
                    // 일반 세그먼트는 그대로 추가 (특수문자 이스케이프 처리)
                    regex.append(Pattern.quote(segment));
                }
            }
            regex.append("$");  // 정규표현식 끝

            return new CompiledPath(Pattern.compile(regex.toString()), paramNames);
        }

        /**
         * 들어온 요청이 이 라우트와 매칭되는지 확인
         *
         * 매칭 조건:
         * 1. HTTP 메서드가 동일해야 함
         * 2. URL 패턴이 정규표현식과 매칭되어야 함
         * 3. 매칭 성공시 경로 변수들을 추출하여 pathVarsOut에 저장
         *
         * @param requestMethod 요청 HTTP 메서드
         * @param requestPath   요청 경로
         * @param pathVarsOut   경로 변수를 저장할 맵 (출력 파라미터)
         * @return 매칭 성공 여부
         */
        boolean matches(String requestMethod, String requestPath, Map<String, String> pathVarsOut) {
            // 1단계: HTTP 메서드 확인 (GET, POST, PUT, DELETE)
            if (!this.method.equals(requestMethod)) {
                return false;
            }

            // 2단계: 경로 패턴 매칭 (정규표현식)
            Matcher matcher = pathPattern.matcher(requestPath);
            if (!matcher.matches()) {
                return false;
            }

            // 3단계: 경로 변수 추출
            // Named Capturing Group에서 값을 추출하여 pathVarsOut에 저장
            for (String paramName : paramNames) {
                pathVarsOut.put(paramName, matcher.group(paramName));
            }

            return true;
        }
    }

    /**
     * 컴파일된 경로 정보를 담는 내부 클래스
     *
     * compilePath() 메서드의 결과를 담는 불변 객체
     */
    private static class CompiledPath {
        final Pattern pattern;           // 컴파일된 정규표현식 패턴
        final List<String> paramNames;  // 경로 변수 이름들

        CompiledPath(Pattern pattern, List<String> paramNames) {
            this.pattern = pattern;
            this.paramNames = paramNames;
        }
    }

    /**
     * 매칭 결과를 담는 record 클래스
     *
     * Java 14에서 도입된 record는 불변 데이터를 담는 간결한 방법입니다.
     * 자동으로 생성되는 것들:
     * - 생성자: new Matched(handler, pathVars)
     * - 접근자 메서드: handler(), pathVars()
     * - equals(), hashCode(), toString()
     *
     * 사용 예시:
     * Matched result = new Matched(userHandler, Map.of("id", "123"));
     * RouteHandler handler = result.handler();
     * Map<String, String> vars = result.pathVars();
     *
     * @param handler  매칭된 라우트의 핸들러
     * @param pathVars 추출된 경로 변수들 (예: {id: "123", postId: "456"})
     */
    public record Matched(RouteHandler handler, Map<String, String> pathVars) {}
}
