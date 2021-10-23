import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    public void happyPathTest() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Встреча успешно забронирована на")).shouldBe(appear, Duration.ofSeconds(15));
    }


    //Тесты для проверки валидации
    @Test
    public void allFieldsAreEmpty() {
        $("[data-test-id='city'] [class='input__control']").setValue("");
        $("[data-test-id='date'] [class='input__control']").doubleClick().sendKeys(" ");
        $(withText("Мы гарантируем безопасность ваших данных")).click(); //для того, чтобы скрыть календарь
        $("[data-test-id='name'] [class='input__control']").setValue("");
        $("[data-test-id='phone'] [class='input__control']").setValue("");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(appear);
    }

    @Test
    public void allValuesAreWrong() {
        $("[data-test-id='city'] [class='input__control']").setValue("New York");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] [class='input__control']").setValue("89001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear);
    }

    //Поле "Город"
    @Test
    public void emptyCityField() {
        $("[data-test-id='city'] [class='input__control']").setValue("");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(appear);
    }

    @Test
    public void notAllowedCity() {
        $("[data-test-id='city'] [class='input__control']").setValue("Реутов");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear);
    }

    @Test
    public void enteringCityUsingNumbers() {
        $("[data-test-id='city'] [class='input__control']").setValue("12345");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear);
    }

    @Test
    public void enteringCityUsingSpecialChars() {
        $("[data-test-id='city'] [class='input__control']").setValue("!@#$%");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear);
    }

    // Дата может быть только = текущая дата + 3 дня
    @Test
    public void plusThreeDays() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Встреча успешно забронирована на")).shouldBe(appear, Duration.ofSeconds(15));
    }

    @Test
    public void plusTwoDays() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Заказ на выбранную дату невозможен")).shouldBe(appear);
    }

    @Test
    public void plusOneYear() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Встреча успешно забронирована на")).shouldBe(appear, Duration.ofSeconds(15));
    }

    //Поле "Фамилия и имя"
    @Test
    public void emptyNameField() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(appear);
    }

    @Test
    public void enteringSpacesInTheNameField() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("   ");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(appear);
    }

    @Test
    public void enteringNameInEnglish() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(appear);
    }

    @Test
    public void enteringNameUsingNumbers() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("123456");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(appear);
    }

    @Test
    public void enteringNameUsingSpecialChars() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("%#$@%");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(appear);
    }

    //Поле "Мобильный телефон"
    @Test
    public void emptyTelephoneField() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(appear);
    }

    @Test
    public void wrongTelephoneNumber() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("89001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."))
                .shouldBe(appear);
    }

    //Чекбокс
    @Test
    public void orderWithNoCheckbox() {
        $("[data-test-id='city'] [class='input__control']").setValue("Москва");
        $("[data-test-id='date'] [class='input__control']").doubleClick()
                .sendKeys(LocalDate.now().plusDays(4).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $("[data-test-id='name'] [class='input__control']").setValue("Иванов Иван");
        $("[data-test-id='phone'] [class='input__control']").setValue("+79001000110");
        $(byText("Забронировать")).click();
        $("[class='checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid']").shouldBe(visible);
    }
}
