package ru.netology.data;

import com.github.javafaker.Faker;

import java.util.Locale;

public class DataHelper {
    private DataHelper() {
    }

    public static String generateMonthNumber() {
        String[] monthNumbers = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        int itemIndex = (int) (Math.random() * monthNumbers.length);
        return monthNumbers[itemIndex];
    }

    public static String generateYearNumber() {
        String[] yearNumbers = new String[]{"22", "23", "24", "25", "26"};
        int itemIndex = (int) (Math.random() * yearNumbers.length);
        return yearNumbers[itemIndex];
    }

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getFullUsersName() {
        Faker faker = new Faker();
        return faker.name().name().toUpperCase();
    }

    public static String getFullUsersNameInLowCaseLetters() {
        Faker faker = new Faker();
        return faker.name().name().toLowerCase();
    }

    public static String getFullUsersNameInUpperCaseAndLowCaseLetters() {
        Faker faker = new Faker();
        return faker.name().name();
    }

    public static String getFullUsersNameInRussian(String locale) {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().name().toUpperCase(Locale.ROOT);
    }

    public static String getOnlyUsersFirstName() {
        Faker faker = new Faker();
        return faker.name().firstName().toUpperCase();
    }

    public static String getOnlyUsersLastName() {
        Faker faker = new Faker();
        return faker.name().lastName().toUpperCase();
    }

    public static int getCVCNumber() {
        Faker faker = new Faker();
        return faker.number().numberBetween(000, 999);
    }
}
