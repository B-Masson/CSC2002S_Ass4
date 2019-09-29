import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
                private AtomicInteger droppedWords; //tracks dropped words to be handled by WordApp
                private Color col = Color.green; //Used for colour shift
                private int counter;
		
		public void paintComponent(Graphics g) {
		    //System.out.println("Paint called");
                    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
                    g.setColor(col);
		    g.fillRect(0,maxY-10,width,height);
                    

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		    for (int i=0;i<noWords;i++){	    	
		    	g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()-10);	
		    	//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words	
		    }
		   
		  }
		
		WordPanel(WordRecord[] words, int maxY) {
			this.words=words; //will this work?
			noWords = words.length;
			done=false;
			this.maxY=maxY;	
                        droppedWords = new AtomicInteger(0);
		}
                
                public void setDone()
                {
                    done = true;
                }
                
                public void undone()
                {
                    done = false;
                }
                
                public boolean isDone()
                {
                    return done;
                }
                
                public int getDropped()
                {
                    return droppedWords.get();
                }
                
                public void resetDropped()
                {
                    droppedWords.set(0);
                }
		
                public void colourshift(int val) //Change panel colout based on the value for dropped words
                {
                    double rtemp = 255*(val/10.0); //r scales with val
                    int r = (int) rtemp;
                    if (r > 255)r = 255; //capping overflow
                    int g = 255-r; //g scales against it
                    col = new Color(r,g,0); //set the panel colour variable
                }
                
                @Override
                public void run()
                {
                    undone(); //Set thread to be "busy" aka not done
                    Thread[] threads = new Thread[noWords]; //Create thread array
                        for (int i = 0; i < noWords; i++) //For i in words
                        {
                            final WordRecord currentWord = words[i]; //Set word that this thread will handle
                            Runnable thisWord = new Runnable() //Make a unique runnable method for said thread
                            {
                                public void run()
                                {
                                    while (!done) //While game runs
                                    {
                                        try
                                    {
                                        Thread.sleep(100); //Pause
                                    } catch (InterruptedException ex)
                                    {
                                        Logger.getLogger(WordPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    int spd = currentWord.getSpeed(); //Yes this is redundant
                                    currentWord.drop(spd/200); //Drop a bit based on words fall speed
                                    if (currentWord.dropped()) //If it hits the bottom
                                    {                                       
                                        droppedWords.incrementAndGet(); //Increase drop count here (to be updated on the score later - storing it here for colour shift)
                                        colourshift(getDropped()); //Colour shift
                                        currentWord.resetWord(); //Get a new word
                                    }
                                    }
                                } //This will continually loop for a given word ie. given position in the array until game ends
                            };
                            threads[i] = new Thread(thisWord); //Set the rules for this thread
                            threads[i].start(); //Start it up
                        }
                        while (!done)
                        {
                            repaint(); //Keep animating until game is considered "over"
                        } //Individual threads will end here as well
                        resetDropped(); //Clear out dropped value
                        col = Color.green; //Back to starting colour (0,255,0)
                        for (int j = 0; j < noWords; j++)
                        { 
                            words[j].resetWord(); //Don't forget to manually reset each word since the threads will all have been cut off
                        }
                        repaint(); //Reset GUI to default state
                       
                }
                     
                //Old, sequential version of the main run() method. Exists as a relic of the past. Used one thread to handle all words.
                /*@Override
		public void run() {
                    undone();
                    while(!done)
                    {
                        try
                        {
                            Thread.sleep(100);
                        } catch (InterruptedException ex)
                        {
                            Logger.getLogger(WordPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        for (int i = 0; i < noWords; i++)
                        {
                            int spd = words[i].getSpeed();
                            words[i].drop(spd/200);
                            if (words[i].dropped())
                            {
                                droppedWords.incrementAndGet();
                                colourshift(getDropped());
                                words[i].resetWord();
                            }
                            
                        }
                        repaint();
                    }
                    resetDropped();
                    col = Color.green;
                    for (int j = 0; j < noWords; j++)
                    {
                        words[j].resetWord();
                    }
                    repaint();
                    //System.out.println("thread complete");
		}*/
               
	}


