import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
                private AtomicInteger droppedWords; //tracks dropped words to be handled by WordApp
                private Color col = Color.green; //Used for colour shift
		
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
                    double rtemp = 255*(val/10.0);
                    int r = (int) rtemp;
                    if (r > 255)r = 255;
                    int g = 255-r;
                    col = new Color(r,g,0);
                }
                
                @Override
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
                        Thread[] threads = new Thread[noWords];
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
                            /*Runnable eachWord = new Runnable()
                            {                            
                                public void run()
                                {
                                    int spd = words[i].getSpeed();
                                    words[i].drop(spd/200);
                                    if (words[i].dropped())
                                    {
                                        droppedWords++;
                                        colourshift(droppedWords);
                                        words[i].resetWord();
                                    }
                                }
                            };*/
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
		}

	}


