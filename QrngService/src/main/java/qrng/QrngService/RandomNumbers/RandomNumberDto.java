package qrng.QrngService.RandomNumbers;

import java.util.List;

public class RandomNumberDto {
    private List<Byte> numbers;

    public RandomNumberDto() {
    }

    public RandomNumberDto(List<Byte> numbers) {
        this.numbers = numbers;
    }

    public void setNumbers(List<Byte> numbers) {
        this.numbers = numbers;
    }

    public List<Byte> getNumbers() {
        return this.numbers;
    }
}
