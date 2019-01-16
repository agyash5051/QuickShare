/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickshare;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;


/**
 *
 * @author Yash Agarwal
 */
class ReceiveThread extends Thread {

    Socket skt = null;
    ServerSocket sskt = null;
    DataInputStream dis = null;
    FileOutputStream fos = null;
    
    

    @Override
    public void run() {

        try {
            sskt = new ServerSocket(2888);
            skt = sskt.accept();
            int i=0;
            if (skt != null) {
                dis = new DataInputStream(skt.getInputStream());
                String filename = dis.readUTF();
                long size = Long.parseLong(dis.readUTF());
                File f = new File(filename);
                if(f.exists()){
                    i = JOptionPane.showConfirmDialog(null, "Confirm", "Override", JOptionPane.OK_CANCEL_OPTION);
                    System.out.println("Test"+i);
                }
                if(i==0)
                {
                    
                
                System.out.println("FIlename " + filename);
                fos = new FileOutputStream(filename);
                byte ba[] = new byte[2048];
                int a;
                while ((a = dis.read(ba)) != -1) {
                    fos.write(ba, 0, a);
                }
                f = new File(filename);
                if(f.length()==size)
                {
                    double size_mb = ((double)size / (1024*1024));
                    String size_in_mb = String.format("%.2f", size_mb);
                    JOptionPane.showMessageDialog(MainForm.mf, "File Received"+"\n"+filename+": "+ size_in_mb + " MB", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(MainForm.mf, "File Receive Aborted", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                
                }
                else
                {
                    JOptionPane.showMessageDialog(MainForm.mf, "File Not Received", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        } catch (Exception e) {
            System.out.println("Exception Receive " + e);
        } finally {
            try {
                fos.close();
                dis.close();
                skt.close();
                sskt.close();
                MainForm.btnreceive.setEnabled(true);

            } catch (Exception e) {
                System.out.println("Receive Thread Finally " + e);
            }
        }

    }
    
    public void releaseResource()
    {
                try {
                    MainForm.btnreceive.setEnabled(true);
                    sskt.close();
                    skt.close();
                    dis.close();
                    fos.close();
                
                
                
                
                

            } catch (Exception e) {
                System.out.println("Release Resource " + e);
            }
    }

}
