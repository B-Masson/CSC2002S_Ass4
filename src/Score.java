
import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private int missedWords;
	private int caughtWords;
	private int gameScore;
        private AtomicInteger highscore;
        private String highscoreName;
	
	Score() {
		missedWords=0;
		caughtWords=0;
		gameScore=0;
                highscore = new AtomicInteger(0);
	}
		
	
	public synchronized int getMissed() {
		return missedWords;
	}

	public synchronized int getCaught() {
		return caughtWords;
	}
	
	public synchronized int getTotal() {
		return (missedWords+caughtWords);
	}

	public synchronized int getScore() {
		return gameScore;
	}
	
	public synchronized void missedWord() {
		missedWords++;
	}
        
        public synchronized void setMissed(int val)
        {
            missedWords = val;
        }

	public synchronized void caughtWord(int length) {
		caughtWords++;
		gameScore+=length;
	}

	public synchronized void resetScore() {
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
        
        public void setHigh(int high) //Atomically set our highscore value
        {
            highscore.set(high);
        }
        
        public int getHigh() //Atomically grab value
        {
            return highscore.get();
        }
        
        public void setName(String n) //Set name for highscorer
        {
            highscoreName = n;
        }
        
        public String getName() //Grab the name
        {
            return highscoreName;
        }
}
