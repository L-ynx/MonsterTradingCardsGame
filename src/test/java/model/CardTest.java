package model;

import org.fhtw.application.model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    // Test monster and element type of spell card
    @Test
    void spellCard() {
        Card waterSpell = provideCards("WaterSpell");
        Card fireSpell = provideCards("FireSpell");
        Card regularSpell = provideCards("RegularSpell");

        waterSpell.getElements();
        fireSpell.getElements();
        regularSpell.getElements();

        assertFalse(waterSpell.isMonster_type());
        assertEquals("Water", waterSpell.getElement_type());
        assertFalse(fireSpell.isMonster_type());
        assertEquals("Fire", fireSpell.getElement_type());
        assertFalse(regularSpell.isMonster_type());
        assertEquals("Regular", regularSpell.getElement_type());
    }

    // Test monster and elementtype of monster card
    @Test
    void monsterCard() {
        Card waterGoblin = provideCards("WaterGoblin");
        Card fireDragon = provideCards("FireDragon");
        Card knight = provideCards("Knight");

        fireDragon.getElements();
        waterGoblin.getElements();
        knight.getElements();

        assertTrue(waterGoblin.isMonster_type());
        assertEquals("Water", waterGoblin.getElement_type());
        assertTrue(fireDragon.isMonster_type());
        assertEquals("Fire", fireDragon.getElement_type());
        assertTrue(knight.isMonster_type());
        assertEquals("Regular", knight.getElement_type());
    }

    private static Card provideCards(String cardName) {
        Card card = new Card();
        card.setCardName(cardName);

        return card;
    }
}
