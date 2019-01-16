/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickshare;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author Yash Agarwal
 */
class SendThread extends Thread{
    DataOutputStream dos = null;
    FileInputStream fis = null;
    File f;
    Socket skt;
    SendForm sf;
    public SendThread(Socket skt,File f,SendForm sf)
    {
        this.skt=skt;
        this.f=f;
        this.sf=sf;
    }
    @Override
    public void run()
    {
        try {
                dos = new DataOutputStream(skt.getOutputStream());
                if(f==null)
                        {
                            String filename= SendForm.txtfilepath.getText();
                            f = new File(filename);
                        }
                
                if (f.exists()) {
                    dos.writeUTF(f.getName());
                    dos.writeUTF(Long.toString(f.length()));
                    fis = new FileInputStream(f);
                    byte ba[] = new byte[2048];
                    int a;
                    SendForm.txtstatus.setText("File Transfer in Progress");
                    while ((a = fis.read(ba)) != -1) {
                        dos.write(ba, 0, a);
                    }
                    fis.close();
                    dos.close();
                    double size_mb = ((double)f.length() / (1024*1024));
                    String size_in_mb = String.format("%.2f", size_mb);
                    JOptionPane.showMessageDialog(sf, "File Sent Successfully."+"\n"+f.getName()+": "+ size_in_mb+" MB", "Success", JOptionPane.INFORMATION_MESSAGE);
                    SendForm.txtstatus.setText("File Transfer Complete");

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(sf, "Transfer Unsuccessful\nPlease check Receiver's connectivity\n", "Warning", JOptionPane.INFORMATION_MESSAGE);
            } finally {
                try {
                    
                    skt.close();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(sf, e, "Exception", JOptionPane.INFORMATION_MESSAGE);
                }
            }
    }
    
}
