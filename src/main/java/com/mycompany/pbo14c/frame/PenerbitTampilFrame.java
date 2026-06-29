package com.mycompany.pbo14c.frame;

import com.mycompany.pbo14c.db.Koneksi;
import com.mycompany.pbo14c.model.Penerbit;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class PenerbitTampilFrame extends JFrame {
    JLabel label1 = new JLabel("Cari");
    JTextField eCari = new JTextField();
    JButton bCari = new JButton("Cari");
    
    String header[] = {"Id","Nama Penerbit"};
    TableModel tableModel = new DefaultTableModel(header,0);
    JTable tPenerbit = new JTable(tableModel);
    JScrollPane jScrollPane = new JScrollPane(tPenerbit);
    
    JButton bTambah = new JButton("Tambah");
    JButton bUbah = new JButton("Ubah");
    JButton bHapus = new JButton("Hapus");
    JButton bBatal = new JButton("Batal");
    JButton bTutup = new JButton("Tutup");
    
    Penerbit penerbit;
    
    public void setKomponen(){
        getContentPane().setLayout(null);
        getContentPane().add(label1);
        getContentPane().add(eCari);
        getContentPane().add(bCari);
        getContentPane().add(jScrollPane);
        getContentPane().add(bTambah);
        getContentPane().add(bUbah);
        getContentPane().add(bHapus);
        getContentPane().add(bBatal);
        getContentPane().add(bTutup);
        
        label1.setBounds(10, 10, 50, 25);
        eCari.setBounds(60, 10, 330, 25);
        bCari.setBounds(400, 10, 70, 25);
        bTutup.setBounds(400, 220, 70, 25);
        bTambah.setBounds(10, 220, 80, 25);
        bUbah.setBounds(95, 220, 70, 25);
        bHapus.setBounds(170, 220, 70, 25);
        bBatal.setBounds(245, 220, 70, 25);
        jScrollPane.setBounds(10, 45, 460, 160);
        
        resetTable("");
        setListener();
        setVisible(true);
    }

    public PenerbitTampilFrame() {
        setSize(500,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setKomponen();
    }
    
    public static void main(String[] args) {
        PenerbitTampilFrame penerbitTampilFrame = new PenerbitTampilFrame();
    }
    
    public ArrayList<Penerbit> getPenerbitList(String keyword){
        ArrayList<Penerbit> penerbitList = new ArrayList<Penerbit>();
        Koneksi koneksi = new Koneksi();
        Connection connection = koneksi.getConnection();
        
        String query = "SELECT * FROM penerbit " + keyword;
        Statement statement;
        ResultSet resultSet;
        
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                penerbit = new Penerbit(
                        resultSet.getInt("id"),
                        resultSet.getString("penerbit")
                );
                
                penerbitList.add(penerbit);
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
        return penerbitList;
    }
    
    public final void selectPenerbit(String keyword){
        ArrayList<Penerbit> list = getPenerbitList(keyword);
        DefaultTableModel model = (DefaultTableModel) tPenerbit.getModel();
        Object[] row = new Object[2]; // sesuaikan dengan jumlah kolom
        
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getPenerbit();
            
            model.addRow(row);
        }
    }
    
    public final void resetTable(String keyword){
        DefaultTableModel model = (DefaultTableModel) tPenerbit.getModel();
        model.setRowCount(0);
        selectPenerbit(keyword);
    }
    
    public void setListener(){
        bTutup.addActionListener((e) -> {
            dispose();
        });
        
        bCari.addActionListener((e) -> {
            //wildcard
            resetTable(" WHERE penerbit LIKE '%" + eCari.getText() + "%'");
        });
        
        bBatal.addActionListener((e) -> {
            resetTable("");
        });
        
        bHapus.addActionListener((e) -> {
            int i = tPenerbit.getSelectedRow();
            if( i>=0 ){
                int pilihan = JOptionPane.showConfirmDialog(
                        null, 
                        "Yakin hapus?",
                        "Konfirmasi hapus",
                        JOptionPane.YES_NO_OPTION);
                if(pilihan == 0){
                    TableModel model = tPenerbit.getModel();
                    Koneksi koneksi = new Koneksi();
                    Connection con = koneksi.getConnection();
                    String deleteSQL = "DELETE FROM penerbit WHERE id=?";
                    try {
                        PreparedStatement ps = con.prepareStatement(deleteSQL);
                        ps.setString(1, model.getValueAt(i, 0).toString());
                        ps.executeUpdate();
                    } catch (SQLException ex) {
                        System.err.println(ex.toString());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data");
            }
            resetTable("");
        });
        
        bUbah.addActionListener((e) -> {
            int i = tPenerbit.getSelectedRow();
            if( i>=0 ){
                TableModel model = tPenerbit.getModel();
                penerbit = new Penerbit();
                penerbit.setId(Integer.parseInt(model.getValueAt(i,0).toString()));
                penerbit.setPenerbit(model.getValueAt(i,1).toString());
                PenerbitTambahFrame f = new PenerbitTambahFrame(penerbit);
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data");
            }
        });
        
        bTambah.addActionListener((e) -> {
            PenerbitTambahFrame f = new PenerbitTambahFrame();
        });
        
        addWindowListener(new java.awt.event.WindowAdapter(){
           public void windowActivated(java.awt.event.WindowEvent evt){
               resetTable("");
           } 
        });
    }
}
