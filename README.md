# MonsterTradingCardsGame (MTCG)
An HTTP/REST-based server that allows users to trade and battle with and against each other.

## Setup

Install Postgres and create a database named mtcg. The default admin of the database is **mtcgadmin** with password **root**.
You can change this in the dbConnection file, which is located in the database folder within the application.

After that you can run the dbCreation.sql file to create the necessary tables.

Feel free to view the MonsterTradingCards.sh file to see how each request is made.


## The Battle Logic
Your cards are split into 2 categories:
#### - Monster Cards

    Cards with active attacks and damage based on an element type (fire, water, normal).
    The element type does not effect pure monster fights.


#### - Spell Cards

    A spell card can attack with an element based spell (again fire, water, normal) which is:
    
      – effective (eg: water is effective against fire, so damage is doubled)
      – not effective (eg: fire is not effective against water, so damage is halved)
      – no effect (eg: normal monster vs normal spell, no change of damage, direct
      comparison between damages) 

  ##### Effectiveness:
  - water -> fire
  - fire -> normal
  - normal -> water


Cards are chosen randomly each round from the deck to compete (this means 1 round is a
battle of 2 cards = 1 of each player). There is no attacker or defender. All parties are equal in
each round. Pure monster fights are not affected by the element type. As soon as 1 spell
cards is played the element type has an effect on the damage calculation of this single
round. Each round the card with higher calculated damage wins. Defeated monsters/spells
of the competitor are removed from the competitor’s deck and are taken over in the deck of
the current player (vice versa). In case of a draw of a round no action takes place (no cards
are moved). Because endless loops are possible we limit the count of rounds to 100.

### The following specialties are to be considered:

- Goblins are too afraid of Dragons to attack.
- Wizards can control Orks, so they are not able to damage them.
- The armor of Knights is so heavy that WaterSpells make them drown them instantly.
- The Kraken is immune against spells.
- The FireElves know Dragons since they were little and can evade their attacks.

2 Players must enter the Lobby to fight each other. If a player is alone in the lobby for more than 10 seconds, he will be kicked out.

## Trading Deals
You can request a trading deal by pushing a card into the store (MUST NOT BE IN THE DECK) and adding a
requirement (Spell or Monster and additionally a type requirement or a minimum damage) 
for the card to trade with (eg: “spell or monster” and “min-damage: 50”)

## Created by
    Raza Ghulam
