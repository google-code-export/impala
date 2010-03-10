package tapestry5.application.pages;

public class Tapestry5Guess {
    
    private int target;

    Object initialize(int target)
    {
      this.target = target;

      return this;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getTarget() {
        return target;
    }

}
