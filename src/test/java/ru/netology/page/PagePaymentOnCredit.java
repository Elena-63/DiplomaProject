package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class PagePaymentOnCredit {
    private SelenideElement heading = $(byText("Кредит по данным карты"));
    private SelenideElement cardNumber = $("[placeholder= '0000 0000 0000 0000']");
    private SelenideElement month = $("[placeholder= '08']");
    private SelenideElement year = $("[placeholder= '22']");
    private SelenideElement cardholder = $$(".input__control").get(3);
    private SelenideElement cardValidationCode = $("[placeholder= '999']");
    private SelenideElement continueButton = $(byText("Продолжить"));
    private SelenideElement successMessage = $(withText("Операция одобрена Банком"));
    private SelenideElement errorMessage = $(withText("Банк отказал в проведении операции"));
    private SelenideElement errorMessageAboutWrongFormat = $(byText("Неверный формат"));
    private SelenideElement errorMessageAboutWrongDateOfExpiry = $(byText("Неверно указан срок действия карты"));
    private SelenideElement errorMessageWithDateOfExpiry = $(byText("Истёк срок действия карты"));
    private SelenideElement errorMessageBecauseOfEmptyField = $(byText("Поле обязательно для заполнения"));

    public PagePaymentOnCredit() {
        heading.shouldBe(Condition.visible);
    }

    public void withCardNumber(String number) {
        cardNumber.setValue(number);
        month.setValue(DataHelper.generateMonthNumber());
        year.setValue(DataHelper.generateYearNumber());
        cardholder.setValue(DataHelper.getFullUsersName());
        cardValidationCode.setValue(String.valueOf(DataHelper.getCVCNumber()));
        continueButton.click();
    }

    public void withMonth(String monthNumber) {
        cardNumber.setValue(DataHelper.getApprovedCardNumber());
        month.setValue(monthNumber);
        year.setValue(DataHelper.generateYearNumber());
        cardholder.setValue(DataHelper.getFullUsersName());
        cardValidationCode.setValue(String.valueOf(DataHelper.getCVCNumber()));
        continueButton.click();
    }

    public void withYear(String yearNumber) {
        cardNumber.setValue(DataHelper.getApprovedCardNumber());
        month.setValue(DataHelper.generateMonthNumber());
        year.setValue(yearNumber);
        cardholder.setValue(DataHelper.getFullUsersName());
        cardValidationCode.setValue(String.valueOf(DataHelper.getCVCNumber()));
        continueButton.click();
    }

    public void withCardholder(String nameOfCardholder) {
        cardNumber.setValue(DataHelper.getApprovedCardNumber());
        month.setValue(DataHelper.generateMonthNumber());
        year.setValue(DataHelper.generateYearNumber());
        cardholder.setValue(nameOfCardholder);
        cardValidationCode.setValue(String.valueOf(DataHelper.getCVCNumber()));
        continueButton.click();
    }

    public void withCardValidationCode(String cvc) {
        cardNumber.setValue(DataHelper.getApprovedCardNumber());
        month.setValue(DataHelper.generateMonthNumber());
        year.setValue(DataHelper.generateYearNumber());
        cardholder.setValue(DataHelper.getFullUsersName());
        cardValidationCode.setValue(cvc);
        continueButton.click();
    }

    public void emptyFields() {
        continueButton.click();
    }

    public void waitSuccessMessage() {

        successMessage.waitUntil(Condition.visible, 15000);
    }

    public void waitErrorMessage() {

        errorMessage.waitUntil(Condition.visible, 15000);
    }

    public void waitErrorMessageAboutWrongFormat() {

        errorMessageAboutWrongFormat.waitUntil(Condition.visible, 5000);
    }

    public void waitErrorMessageAboutWrongDateOfExpiry() {
        errorMessageAboutWrongDateOfExpiry.waitUntil(Condition.visible, 5000);
    }

    public void waitErrorMessageWithDateOfExpiry() {
        errorMessageWithDateOfExpiry.waitUntil(Condition.visible, 5000);
    }

    public void waitErrorMessageBecauseOfEmptyField() {
        errorMessageBecauseOfEmptyField.waitUntil(Condition.visible, 10000);
    }
}