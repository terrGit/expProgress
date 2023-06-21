package com.example.expprogress;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.ResourceBundle;

public class HelloController {
    @FXML
    private TableColumn<getHistory, String> col_id;
    @FXML
    private TableColumn <getHistory, String>col_waktu;
    @FXML
    private TableColumn<getHistory, String> col_deskripsi;
    @FXML
    private TableColumn <getHistory, String>col_nota;
    @FXML
    private TableColumn <getHistory, String>col_jasa;
    @FXML
    private TableView<getHistory> table;

    @FXML
    private TextField t_id;
    @FXML
    private TextField t_nota;
    @FXML
    private TextField t_jasa;
    @FXML
    private TextField t_deskripsi;

Alert alert;


@FXML
    public void insertBtn(){
        if(t_id.getText().isEmpty()|| t_nota.getText().isEmpty() ||
                t_jasa.getText().isEmpty()|| t_deskripsi.getText().isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR BRO");
            alert.setHeaderText(null);
            alert.setContentText("ISI DULU SEMUA TEXT FIELD NYA");
            alert.showAndWait();
        }else {

            try {
                Connection connect = HelloApplication.createDatabaseConnection();
                String masuk = "INSERT INTO history_detail_pesanan(`id_history`, `waktu`, `deskripsi`, `nota_id`, `jasa_id`) VALUES (?,?,?,?,?)";
                PreparedStatement prepare;
                prepare = connect.prepareStatement(masuk);
                prepare.setString(1, t_id.getText());
                Date date = new Date();
                long millis = date.getTime();
                Timestamp timestamp = new Timestamp(millis);
                //untuk mendapatkan tanggal dan waktu saat ini
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(2, String.valueOf(timestamp));
                prepare.setString(3, t_deskripsi.getText());
                prepare.setString(4, t_nota.getText());
                prepare.setString(5, t_jasa.getText());
                prepare.executeUpdate();
                historyShowData();
                connect.close();


            }catch (Exception e){e.printStackTrace();}
        }
    }

    public HelloController() {
    }


    public ObservableList<getHistory> data (){
    String SQL = "SELECT * FROM history_detail_pesanan";

    ObservableList<getHistory> data = FXCollections.observableArrayList();
    try {

        Connection connect = HelloApplication.createDatabaseConnection();
        Statement statement = connect.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM  history_detail_pesanan");;
        getHistory getH;
        int column_count = result.getMetaData().getColumnCount();
        if(column_count > 0) // ada data
        {
        while (result.next()){
            getH = new getHistory(result.getInt("id_history"),
                    result.getTimestamp("waktu"),
                    result.getString("deskripsi"),
                    result.getInt("nota_id"),
                    result.getInt("jasa_id"));

        data.add(getH); }
        connect.close();
        }

    }catch (Exception e){e.printStackTrace();}
    return data;
}

private ObservableList<getHistory> historyListData;
//UNTUK MENUNJUKKAN DATA DI TABLEVIEW
public void historyShowData(){
    historyListData =  data();

    col_id.setCellValueFactory(new PropertyValueFactory<>("id_history"));
    col_waktu.setCellValueFactory(new PropertyValueFactory<>("waktu"));
    col_deskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
    col_nota.setCellValueFactory(new PropertyValueFactory<>("nota_id"));
    col_jasa.setCellValueFactory(new PropertyValueFactory<>("jasa_id"));

    table.setItems(historyListData);

}

    @FXML
    protected void update(){
        try {
            Connection con = HelloApplication.createDatabaseConnection();
            String query = "select count(1) from  history_detail_pesanan where id_history = '" + t_id.getText() + "'";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
              if(rs.getInt(1) == 1){
                  Date date = new Date();
                  long millis = date.getTime();
                  Timestamp timestamp = new Timestamp(millis);
                    //untuk mendapatkan tanggal dan waktu saat ini

                    String SQL = "UPDATE history_detail_pesanan SET waktu = '"
                            +timestamp +"',deskripsi='"+t_deskripsi.getText() + "'" +
                            " WHERE id_history = '" + t_id.getText() + "'";

                    PreparedStatement preparedStatement = con.prepareStatement(SQL);
                   preparedStatement.executeUpdate();
                    historyShowData();
                System.out.println("Berhasil");
                }
                else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR BRO");
                    alert.setHeaderText(null);
                    alert.setContentText("ID belum di isi atau ID tidak sesuai");
                    alert.showAndWait();
                }
            }
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



public void Initialize(){
    historyShowData();

}



}