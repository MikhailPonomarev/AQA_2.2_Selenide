import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.*;

public class CardDeliveryTest {

    public String deliveryDateInDays(int daysToDelivery) {
        return LocalDate.now().plusDays(daysToDelivery)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String deliveryDateInMonths(int monthsToDelivery) {
        return LocalDate.now().plusMonths(monthsToDelivery)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String deliveryDateInYears(int yearsToDelivery) {
        return LocalDate.now().plusYears(yearsToDelivery).
                format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    public void happyPathTest() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(3));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInDays(3)));
    }


    //Тесты для проверки валидации
    @Test
    public void allFieldsAreEmpty() {
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").doubleClick().sendKeys(" ");
        $(withText("Мы гарантируем безопасность ваших данных")).click(); //для того, чтобы скрыть календарь
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).should(appear);
    }

    @Test
    public void allValuesAreWrong() {
        $("[data-test-id='city'] input").setValue("New York");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(2));
        $("[data-test-id='name'] input").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] input").setValue("89001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).should(appear);
    }

    //Поле "Город"
    @Test
    public void emptyCityField() {
        $("[data-test-id='city'] input").setValue("");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).should(appear);
    }

    @Test
    public void notAllowedCity() {
        $("[data-test-id='city'] input").setValue("Реутов");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).should(appear);
    }

    @Test
    public void enteringCityUsingNumbers() {
        $("[data-test-id='city'] input").setValue("12345");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).should(appear);
    }

    @Test
    public void enteringCityUsingSpecialChars() {
        $("[data-test-id='city'] input").setValue("!@#$%");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Доставка в выбранный город недоступна")).shouldBe(appear);
    }

    // Дата может быть только = текущая дата + 3 дня
    @Test
    public void plusFourDays() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(4));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInDays(4)));
    }

    @Test
    public void plusTwoDays() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(2));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Заказ на выбранную дату невозможен")).should(appear);
    }

    @Test
    public void plusOneMonth() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInMonths(1));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInMonths(1)));
    }

    @Test
    public void plusOneYear() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInYears(1));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInYears(1)));
    }

    //Поле "Фамилия и имя"
    @Test
    public void emptyNameField() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).should(appear);
    }

    @Test
    public void enteringSpacesInTheNameField() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("   ");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).should(appear);
    }

    @Test
    public void enteringNameInEnglish() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Ivanov Ivan");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .should(appear);
    }

    @Test
    public void enteringNameUsingNumbers() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("123456");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .should(appear);
    }

    @Test
    public void enteringNameUsingSpecialChars() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("%#$@%");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .should(appear);
    }

    //Поле "Мобильный телефон"
    @Test
    public void emptyTelephoneField() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения")).shouldBe(appear);
    }

    @Test
    public void wrongTelephoneNumber() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("89001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."))
                .should(appear);
    }

    //Чекбокс
    @Test
    public void orderWithNoCheckbox() {
        $("[data-test-id='city'] input").setValue("Москва");
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(byText("Забронировать")).click();
        $("[class='checkbox checkbox_size_m checkbox_theme_alfa-on-white input_invalid']").shouldBe(visible);
    }

    //Выбор города из выпадающего списка
    @Test
    public void selectFirstCityFromDropdown() {
        $("[data-test-id='city'] input").setValue("ни");
        $$("[class='menu-item__control']").first().click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInDays(5)));
    }

    @Test
    public void selectLastCityFromDropdown() {
        $("[data-test-id='city'] input").setValue("ни");
        $$(".menu-item__control").last().click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInDays(5)));
    }

    @Test
    public void selectCityFromTheMiddle() {
        $("[data-test-id='city'] input").setValue("ни");
        $$(".menu-item__control").find(exactText("Нижний Новгород")).click();
        $("[data-test-id='date'] input").doubleClick().sendKeys(deliveryDateInDays(5));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79001000110");
        $(".checkbox__box").click();
        $(byText("Забронировать")).click();
        $(".notification__content").should(appear, Duration.ofSeconds(15))
                .shouldHave(exactText("Встреча успешно забронирована на " + deliveryDateInDays(5)));
    }
}

