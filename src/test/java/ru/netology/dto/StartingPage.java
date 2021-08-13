package ru.netology.dto;

import com.codeborne.selenide.SelenideElement;
import ru.netology.page.PagePayment;
import ru.netology.page.PagePaymentOnCredit;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class StartingPage {
    private SelenideElement buyWithCardButton = $(byText("Купить"));
    private SelenideElement buyWithCreditButton = $(byText("Купить в кредит"));

    public PagePayment buyWithCard() {
        buyWithCardButton.click();
        return new PagePayment();
    }

    public PagePaymentOnCredit buyWithCredit() {
        buyWithCreditButton.click();
        return new PagePaymentOnCredit();
    }
}