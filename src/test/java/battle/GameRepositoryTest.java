package battle;

import org.fhtw.application.model.Card;
import org.fhtw.application.repository.CardRepository;
import org.fhtw.application.repository.GameRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameRepositoryTest {

    private static GameRepository gameRepo;

    @BeforeAll
    static void initGameRepo() {
        CardRepository cardRepo = new CardRepository();
        gameRepo = new GameRepository(cardRepo);
    }

    // Test normal monster vs monster battle
    @ParameterizedTest
    @MethodSource("provideMons")
    void monVSmon(String card1Name, String card2Name, float card1Dmg, float card2Dmg) throws Exception {

        Card card1 = new Card();
        card1.setCardName(card1Name);
        card1.setDamage(card1Dmg);
        card1.setMonster_type(true);

        Card card2 = new Card();
        card2.setCardName(card2Name);
        card2.setDamage(card2Dmg);
        card2.setMonster_type(true);

        Method fight = gameRepo.getClass().getDeclaredMethod("fight", Card.class, Card.class);
        fight.setAccessible(true);

        Object battleOutcome = fight.invoke(gameRepo, card1, card2);

        assertEquals(GameRepository.BattleOutcome.PLAYER1_WIN, battleOutcome);
    }

    // Test all the special cases
    @ParameterizedTest
    @MethodSource("provideSpecial")
    void specialFight(String card1Name, String card2Name, float card1Dmg, float card2Dmg) throws Exception {
        Card card1 = new Card();
        card1.setCardName(card1Name);
        card1.setDamage(card1Dmg);

        Card card2 = new Card();
        card2.setCardName(card2Name);
        card2.setDamage(card2Dmg);

        Method fight = gameRepo.getClass().getDeclaredMethod("fight", Card.class, Card.class);
        fight.setAccessible(true);

        Object battleOutcome = fight.invoke(gameRepo, card1, card2);

        assertEquals(GameRepository.BattleOutcome.PLAYER2_WIN , battleOutcome);
    }

    // Test element effectiveness
    @ParameterizedTest
    @MethodSource("provideElements")
    void elementFight(String card1Name, String card2Name, float card1Dmg, float card2Dmg, String element1, String element2) throws Exception {
        Card card1 = new Card();
        card1.setCardName(card1Name);
        card1.setDamage(card1Dmg);
        card1.setElement_type(element1);

        Card card2 = new Card();
        card2.setCardName(card2Name);
        card2.setDamage(card2Dmg);
        card2.setElement_type(element2);

        Method fight = gameRepo.getClass().getDeclaredMethod("fight", Card.class, Card.class);
        fight.setAccessible(true);

        Object battleOutcome = fight.invoke(gameRepo, card1, card2);

        assertEquals(GameRepository.BattleOutcome.PLAYER2_WIN , battleOutcome);
    }

    @Test
    void player1Win() throws Exception {

        List<Card> player1Deck = createDeck(100.0f, "Water", "Fire");
        List<Card> player2Deck = createDeck(10.0f, "Fire", "Regular");

        Method battle = gameRepo.getClass().getDeclaredMethod("battle", String.class, String.class, List.class, List.class);
        battle.setAccessible(true);

        Object battleOutcome = battle.invoke(gameRepo, "Ash", "Gary", player1Deck, player2Deck);

        assertEquals(GameRepository.BattleOutcome.PLAYER1_WIN , battleOutcome);
    }

    @Test
    void player2Win() throws Exception {

        List<Card> player1Deck = createDeck(10.0f, "Water", "Fire");
        List<Card> player2Deck = createDeck(50.0f, "Fire", "Regular");

        Method battle = gameRepo.getClass().getDeclaredMethod("battle", String.class, String.class, List.class, List.class);
        battle.setAccessible(true);

        Object battleOutcome = battle.invoke(gameRepo, "Ash", "Gary", player1Deck, player2Deck);

        assertEquals(GameRepository.BattleOutcome.PLAYER2_WIN , battleOutcome);
    }


    private static Stream<Arguments> provideMons() {
        return Stream.of(
                Arguments.of("Dragon", "Goblin", 50.0f, 10.0f),
                Arguments.of("Knight", "Kraken", 10.0f, 5.0f),
                Arguments.of("Elf", "Wizard", 21.0f, 20.0f)
        );
    }

    private static Stream<Arguments> provideSpecial() {
        return Stream.of(
                Arguments.of("Goblin", "Dragon", 100.0f, 20.0f),
                Arguments.of("Ork", "Wizzard", 1000.0f, 5.0f),
                Arguments.of("Knight", "WaterSpell", 210.0f, 20.0f),
                Arguments.of("Dragon", "FireElf", 21.0f, 1.0f),
                Arguments.of("WaterSpell", "Kraken", 21.0f, 80.0f),
                Arguments.of("FireSpell", "Kraken", 4.0f, 20.0f),
                Arguments.of("RegularSpell", "Kraken", 29.0f, 6.0f)
        );
    }

    private static Stream<Arguments> provideElements() {
        return Stream.of(
                Arguments.of("FireGoblin", "WaterDragon", 50.0f, 50.0f, "Fire", "Water"),
                Arguments.of("WaterDragon", "Knight", 50.0f, 50.0f, "Water", "Regular"),
                Arguments.of("Knight", "FireSpell", 50.0f, 50.0f, "Regular", "Fire")
        );
    }

    private List<Card> createDeck(float damage, String element1, String element2) {
        List<Card> deck = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Card card = new Card();
            card.setCardName("Card" + (i+1));
            card.setDamage(damage);
            card.setElement_type(i % 2 == 0 ? element1 : element2);
            deck.add(card);
        }

        return deck;
    }
}
