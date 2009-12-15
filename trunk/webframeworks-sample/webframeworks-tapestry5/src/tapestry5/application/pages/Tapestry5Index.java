package tapestry5.application.pages;

import java.util.Random;

import org.apache.tapestry5.annotations.InjectPage;

public class Tapestry5Index {

      private final Random random = new Random();

      @InjectPage
      private Tapestry5Guess guess;

      Object onAction()
      {
        int target = random.nextInt(10) + 1;

        return guess.initialize(target);
      }
}
