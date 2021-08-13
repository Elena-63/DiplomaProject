package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.dto.StartingPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayOnCreditTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void shouldOpen() {
        String sutUrl = System.getProperty("sut.url");
        open(sutUrl);
    }

    @AfterEach
    void shouldClearAll() {
        SQLHelper.shouldDeleteAfterPayment();
    }

    @Test
    @DisplayName("Покупка с APPROVED картой")
    void shouldBuySuccessfullyWithApprovedCard() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var number = DataHelper.getApprovedCardNumber();
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitSuccessMessage();
        var paymentWithCreditInfo = SQLHelper.getPaymentWithCreditInfo();
        assertEquals("APPROVED", paymentWithCreditInfo.getStatus());
    }

    @Test
    @DisplayName("Покупка с DECLINED картой")
    void shouldNotSellWithDeclinedCard() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var number = DataHelper.getDeclinedCardNumber();
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
        var paymentWithCreditInfo = SQLHelper.getPaymentWithCreditInfo();
        assertEquals("DECLINED", paymentWithCreditInfo.getStatus());
    }

    @Test
    @DisplayName("Отправка пустой формы")
    void shouldNotSellWhenAllFieldsAreEmpty() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        buyWithCreditPage.emptyFields();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
        buyWithCreditPage.waitErrorMessageBecauseOfEmptyField();
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("Поле CVC содержит одну цифру")
    void shouldNotSellWhenCardValidationCodeIsTwoDigitsShort() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var cvc = "1";
        buyWithCreditPage.withCardValidationCode(cvc);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("Поле CVC содержит две цифры")
    void shouldNotSellWhenCardValidationCodeIsOneDigitShort() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var cvc = "12";
        buyWithCreditPage.withCardValidationCode(cvc);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("Поле владелец содержит только Фамилию")
    void shouldNotSellWhenNameOfCardholderIsOnlyLastName() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = DataHelper.getOnlyUsersLastName();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("Поле владелец содержит только Имя")
    void shouldNotSellWhenNameOfCardholderIsOnlyFirstName() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = DataHelper.getOnlyUsersFirstName();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("Срок карты истек")
    void shouldNotSellWhenYearNumberIsLowerThanAllowed() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var yearNumber = "20";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageWithDateOfExpiry();
    }

    @Test
    @DisplayName("Срок карты слишком большой")
    void shouldNotSellWhenYearNumberExceedsTheAllowed() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var yearNumber = "99";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    @DisplayName("В поле год введено одну цифру")
    void shouldNotSellWhenYearNumberIsOneDigitalShort() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var yearNumber = "2";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле год введено 00")
    void shouldNotSellWhenYearNumberIsZeros() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var yearNumber = "00";
        buyWithCreditPage.withYear(yearNumber);
        buyWithCreditPage.waitErrorMessageWithDateOfExpiry();
    }

    @Test
    @DisplayName("Номер карты состоит из 0")
    void shouldNotSellWhenCardNumberIsZeros() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var number = "0000 0000 0000 0000";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
    }

    @Test
    @DisplayName("В поле номер карты введено невалидные данные карты")
    void shouldNotSellWhenCardNumberIsUnknown() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var number = "4444 4444 4444 4443";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessage();
    }

    @Test
    @DisplayName("В поле номер карты введено меньше 16 цифр")
    void shouldNotSellWhenCardNumberIsShort() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var number = "4444 4444 4444 444";
        buyWithCreditPage.withCardNumber(number);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле месяц введено 00")
    void shouldNotSellWhenMonthNumberIsZeros() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var monthNumber = "00";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    @DisplayName("В поле месяц введено одну цифру")
    void shouldNotSellWhenMonthNumberIsOneDigitShort() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var monthNumber = "2";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле месяц введено несуществующий месяц")
    void shouldNotSellWhenMonthNumberExceedsTheAllowed() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var monthNumber = "13";
        buyWithCreditPage.withMonth(monthNumber);
        buyWithCreditPage.waitErrorMessageAboutWrongDateOfExpiry();
    }

    @Test
    @DisplayName("В поле владелец введено одну букву")
    void shouldNotSellWhenNameOfCardholderIsOnlyOneLetter() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = "L";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле владелец введено много букв")
    void shouldNotSellWhenNameOfCardholderHasLotsOfLetters() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = "IWJDNRYFBSYRHFYTVCPQZMSHRBD TGFJVNCMDKELWOQIAJZNDTMDLMREW";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле владелец введено данные строчными буквами")
    void shouldNotSellWhenNameOfCardholderInLowerCaseLetters() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = DataHelper.getFullUsersNameInLowCaseLetters();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле владелец введено данные прописными и строчными буквами")
    void shouldNotSellWhenNameOfCardholderInUpperCaseAndLowerCaseLetters() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = DataHelper.getFullUsersNameInUpperCaseAndLowCaseLetters();
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле владелец введено данные на русском языке")
    void shouldNotSellWhenNameOfCardholderIsInRussian() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = DataHelper.getFullUsersNameInRussian("ru");
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле владелец введено цифры")
    void shouldNotSellWhenNameOfCardholderInContainsNumbers() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = "1234567890";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }

    @Test
    @DisplayName("В поле владелец введено спецсимволы")
    void shouldNotSellWhenNameOfCardholderInContainsSpecialCharacters() {
        var startingPage = new StartingPage();
        var buyWithCreditPage = startingPage.buyWithCredit();
        var nameOfCardHolder = "!@#$%^&*";
        buyWithCreditPage.withCardholder(nameOfCardHolder);
        buyWithCreditPage.waitErrorMessageAboutWrongFormat();
    }
}