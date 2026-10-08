import java.util.random.*;

public class View {

    RandomGenerator Random = RandomGenerator.getDefault();

    public void nowPrint(String text) {
        System.out.print(text);
    }

    // prints text one character at a time with a delay effect. 
    // makes it spookier
    public void Print(String text) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try {
                delay(Random.nextInt(15, 60));
            } catch (Exception e) {
                System.out.print(text.substring(text.indexOf(c))); // the rest of the text
                break;
            }
        }
        System.out.println();
    }

    public void dramaticPrint(String text1, int delay, String text2) {
        for (char c : text1.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try {
                delay(Random.nextInt(15, 60));
            } catch (Exception e) {
                break;
            }
        }
        delay(delay);

        for (char c : text2.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            try {
                Thread.sleep(Random.nextInt(15, 60));
            } catch (Exception e) {
                break;
            }
        }
        System.out.println();

    }


    public void endGame() {
        Print("A ticking sound grows louder, ending in a deafening ringing of a bell.");
        delay(700);

        dramaticPrint("You wake up in a cold sweat...", 500, " in your own bedroom.");
        delay(700);
        dramaticPrint("It was all a nightmare, and now it's over...", 500, " or is it?");
        delay(700);
        Print("Blood drips slowly from the ceiling above your bed.");
        delay(700);
        dramaticPrint("The nightmare continues...", 500, " better luck next time.");
    }

    public void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
        }
    }
}