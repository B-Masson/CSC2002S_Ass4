public class Score {
	private int missedWords;
	private int caughtWords;
	private int gameScore;
        private int highscore;
        private String highscoreName;
	
	Score() {
		missedWords=0;
		caughtWords=0;
		gameScore=0;
                highscore = 0;
	}
		
	// all getters and setters must be synchronized
	
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
        
        public void setHigh(int high)
        {
            highscore = high;
        }
        
        public int getHigh()
        {
            return highscore;
        }
        
        public void setName(String n)
        {
            highscoreName = n;
        }
        
        public String getName()
        {
            return highscoreName;
        }
}
