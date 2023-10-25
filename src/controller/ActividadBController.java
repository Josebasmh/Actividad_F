package controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.accessibility.AccessibleResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Persona;

public class ActividadBController implements Initializable{

	@FXML
	private Button btnAgregar;
	
	@FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;
	
	@FXML
    private TableView<Persona> tblTabla;
	
	@FXML
	private TableColumn<Persona, String> tblApellidos;
	
	@FXML
	private TableColumn<Persona, Integer> tblEdad;
	
	@FXML
	private TableColumn<Persona, String> tblNombre;
	
	@FXML
	private TextField txtApellidos;
	
	@FXML
	private TextField txtEdad;
	
	@FXML
	private TextField txtNombre;
	
	// Variables de clase
	static ObservableList<Persona> listaPersonas;
	static Persona p=new Persona("", "", 0);
	
	/*
	 * Método de inicialización
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		listaPersonas = FXCollections.observableArrayList();
		
		tblNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
		tblApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));
		tblEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));		
			
		
		tblTabla.setItems(listaPersonas);		
	}
		
	/*
	 * Método para abrir la ventana 'VentanaNuePer'
	 */
	@FXML
    void agregarPersona(ActionEvent event) {
		p.setNombre("");
		p.setApellidos("");
		p.setEdad(0);
		crearVentanaAux();
    }
	
	/*
	 * Método para eliminar registros de la tabla.
	 * Si no hay ninguno seleccionado, se captura la 'NullPointerException' y muestra una ventana de error. 
	 */
	@FXML
	void eliminarPersona(ActionEvent event) {
		try {
			String sNombreEliminado = tblTabla.getSelectionModel().getSelectedItem().getNombre();
			String sApellidosEliminado = tblTabla.getSelectionModel().getSelectedItem().getApellidos();
			Integer nEdadEliminado = tblTabla.getSelectionModel().getSelectedItem().getEdad();
			listaPersonas.remove(new Persona(sNombreEliminado, sApellidosEliminado, nEdadEliminado));
			ventanaAlerta("I","Persona eliminada correctamente");
		}catch (NullPointerException e) {
			ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
		}		
    }

    @FXML
    void modificarPersona(ActionEvent event) {
    	
    	try {
    		p.setNombre(tblTabla.getSelectionModel().getSelectedItem().getNombre());
        	p.setApellidos(tblTabla.getSelectionModel().getSelectedItem().getApellidos());
        	p.setEdad(tblTabla.getSelectionModel().getSelectedItem().getEdad());
    		crearVentanaAux();
    	}catch(NullPointerException e) {
    		ventanaAlerta("E", "Seleccione un registro de la tabla. Si no lo hay, añada uno.");
    	}
    	
    }
		
	/*
	 * Metodos auxiliares 
	 */
    
    // para mostrar alertas de tipo error o confirmación
	static void ventanaAlerta(String tipoAlerta, String mensaje) {
		Alert alert = null;
		switch (tipoAlerta) {
			case ("E"):
				alert = new Alert(Alert.AlertType.ERROR);
				break;
			case ("I"):
				alert = new Alert(Alert.AlertType.INFORMATION);
		}
        alert.setContentText(mensaje);
        alert.showAndWait();
	}
	void crearVentanaAux() {
		Stage arg0 = new Stage();
		arg0.setTitle("NUEVA PERSONA"); 
		FlowPane aux;
		try {
			aux = (FlowPane)FXMLLoader.load(getClass().getResource("/fxml/NuevaPersona.fxml"));
			Scene scene = new Scene(aux,600,300);
			arg0.setScene(scene);
			arg0.setMinHeight(300);
			arg0.setMinWidth(600);
			arg0.initModality(Modality.APPLICATION_MODAL);
			arg0.show();
		} catch (IOException e) {
			System.out.println("La ventana no se abrió correctamente.");
			e.printStackTrace();
		}
	}
}