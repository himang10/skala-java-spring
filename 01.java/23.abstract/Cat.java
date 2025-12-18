// 구체적인 동물 클래스 (Cat)
class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    // 추상 메서드 구현 (필수)
    @Override
    void makeSound() {
        System.out.println(name + " says: Meow~");
    }
}
