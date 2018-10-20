package hello;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class HelloMIDlet extends MIDlet implements CommandListener {

    private Command exitCommand; // The exit command
    private Display display;     // The display for this MIDlet
    private Command capture;
    private Form firstForm;
    private MyCanvas canv;
    private Command stop;
    public HelloMIDlet() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 0);
        capture = new Command("Capture", Command.OK, 0);
        stop = new Command("Stop", Command.OK, 0);


        canv = new MyCanvas();
        canv.addCommand(stop);
        firstForm = new Form("Eye there");
    }

    public void startApp() {

        firstForm.addCommand(exitCommand);
        firstForm.addCommand(capture);

        firstForm.setCommandListener(this);

        display.setCurrent(firstForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
            SocketConnection connection1=null;
            DataInputStream iStrm=null;
            DataOutputStream dos=null;
            int cvv =0;
    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }
        else if (c== stop)
        {
            thread =0 ;
            try{
            dos.flush();
            dos.write("stop".getBytes());
            display.setCurrent(firstForm);
         }catch(Exception e)
         {
             e.printStackTrace();
         }
        }
        else if ( c == capture)
        {
            System.out.print("Capture");

            //..................................................................
               try{

                   connection1=(SocketConnection) Connector.open("socket://localhost:6666");
                   iStrm=connection1.openDataInputStream();
                   dos=connection1.openDataOutputStream();
                   System.out.print("KKK");

                   String w=String.valueOf(canv.getWidth());
                       String h= String.valueOf(canv.getHeight());
                       System.out.print(w);
                       System.out.print(h);
                       String wh=w+":"+h+".";


                         dos.write(wh.getBytes());

                     }catch(Exception e){
                     e.printStackTrace();}




            //..................................................................



                RunThread runThread = new RunThread();
                runThread.start();




        }
    }


    private Image getImage(DataInputStream iStrm) throws IOException
      {

                 int nBlocks=0;
                ByteArrayOutputStream bStrm = null;
                Image im = null;

                try
                {
                  // ContentConnection includes a length method
                  byte imageData[];
                    bStrm = new ByteArrayOutputStream();

                    byte ch;
                    int i=0;
                    try{
                         nBlocks = iStrm.readInt();
                         System.out.print("Blocks"+nBlocks);
                        for(i=0;i<nBlocks;i++)
                            {
                                int bsize = iStrm.readInt();
                                byte[] temp = new byte[bsize];
                                iStrm.read(temp, 0, bsize);
                                bStrm.write(temp, 0, bsize);
                            }

                    }
                        catch(EOFException e)
                        {
                            System.out.println("EOF Error");
                            e.printStackTrace();
                        }
                            catch(Exception e)
                            {
                                System.out.println("Read Error");
                                e.printStackTrace();
                            }

               imageData = bStrm.toByteArray();
                System.out.println("Out of loop");
               im = Image.createImage(imageData, 0, imageData.length);
              }
            finally
            {
             if (bStrm != null)
                bStrm.close();
            }
                return im;
              }


static int thread = 1;
public class RunThread extends Thread{

                           public void run()
                                 {
                     //             thread=1;
                                       try
                                         {
                                              Image im;
                                              while(thread==1){
                                              if ((im = getImage(iStrm)) != null)
                                                              {
                                                                   canv.img=im;
                                                                   display.setCurrent(canv);
                                                                   canv.repaint();
                                                               }
                                                                    else
                                                                    {
                                                                       Alert alert = new Alert("Error","No image", null, null);
                                                                         alert.setTimeout(Alert.FOREVER);
                                                                         alert.setType(AlertType.ERROR);
                                                                         display.setCurrent(alert);
                                                                    }
                                                  // }


                                              }
                                             }catch (Exception e){System.err.println("Msg: " + e.toString());}


                           }
                //                                                finally
                //                                                {
                //                                                          // Clean up
                //                                                             try{
                //                                                          if (iStrm != null)
                //                                                            iStrm.close();
                //                                                          if (connection != null)
                //                                                            connection.close();}
                //                                                            catch(Exception e){}
                //                                                }

                             }

}
class MyCanvas extends Canvas
{ Image img=null;
    int s=0;
    
   protected void paint(Graphics g) {
                                   
       if(img!=null)
       {
        g.drawImage(img, 0, 0, 0);
        System.out.print("ggggggggggg");

       }
    }
   }



