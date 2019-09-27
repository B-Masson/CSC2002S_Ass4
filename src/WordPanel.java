import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
                private int droppedWords; //tracks dropped words to be handled by WordApp
                private Color col = Color.green;
		
		public void paintComponent(Graphics g) {
		    //System.out.println("Paint called");
                    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
                    g.setColor(col);
		    g.fillRect(0,maxY-10,width,height);
                    

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
		   //draw the words
		   //animation must be added 
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
                        droppedWords = 0;
		}
                
                public void setDone()
                {
                    done = true;
                }
                
                public void undone()
                {
                    done = false;
                }
                
                public synchronized boolean isDone()
                {
                    return done;
                }
                
                public synchronized int getDropped()
                {
                    return droppedWords;
                }
                
                public synchronized void resetDropped()
                {
                    droppedWords = 0;
                }
		
                public synchronized void colourshift(int val)
                {
                    System.out.println("shifting");
                    if (val >= 7)col = Color.red;
                    else if (val >= 5)col = Color.orange;
                    else if (val >= 3)col = Color.yellow;
                    else col = Color.green;
                    System.out.println("to " +col);
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
                                droppedWords++;
                                colourshift(droppedWords);
                                words[i].resetWord();
                            }
                            /*Runnable eachWord = new Runnable()
                            {
                                private int ini = i;                             
                                public void run()
                                {
                                    int spd = words[i].getSpeed();
                                    words[i].drop(spd/200);
                                    if (words[i].dropped())
                                    {
                                        droppedWords++;
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


