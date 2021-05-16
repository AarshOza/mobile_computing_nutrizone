package com.example.nutrizone;

import junit.framework.TestCase;

public class UtilityTest extends TestCase {

    public void testShouldGetValueForGivenSearchString() {
        String input = "% Daily Value-Total Fat 12g-15%-Saturated Fat 7g-35%-Trans Fat 0g\n" +
                "    Polyunsaturated Fat 2g\n" +
                "    Monounsaturated Fat 3g-Cholesterol 10mg-3 %-Sodium 140mg-6%-Total Carbohydrate 40g-15%-0%-Dietary Fiber Og\n" +
                "    Total Sugars 28g-Indludes 28g Added Sugars-56%-Protein 2g-Vitamin D Omog\n" +
                "    Calcium 10mg-0%-0%-Iron 1.1mg-6%-Potassium 50mg-0%-The %Dly Vae DV ypu how muchanent n a-";

        float total_calories = Utility.getQuantity(input, "Total Sugars");

        assertEquals(28.0f, total_calories);
    }

    public void test() {
        String input = "Nutrition Facts 6 servings per container\n" +
                "    Serving size 2 cakes (63g) Amount per serving Calories 280 % Daily Value* Total Fat 12g 15% Saturated Fat 7g 35% Trans Fat 0g Polyunsaturated Fat 2g Monounsaturated Fat 3g Cholesterol 10mg 3% Sodium 140mg 6% Total Carbohydrate 40g 15% Dietary Fiber Og 0% Total Sugars 28g 56% Includes 28g Added Sugars\n" +
                "    Protein 2g Vitamin D Omcg 0% Calcium 1Omg 0% Iron 1.1mg 6% Potassium 50mg\n" +
                "    REDMIFNOTE 5PROanurent n\n" +
                "    MI\"DUALCAMERAL 2.000 calories a day 0% is used tor general hutrition àdvice.";

        float total_calories = Utility.getQuantity(input, "Calories");

        assertEquals(280.0f, total_calories);
    }
}