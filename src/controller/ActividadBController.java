package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
    private Button btnExportar;

    @FXML
    private Button btnImportar;
	
	@FXML
    private TableView<Persona> tblTabla;
	
	@FXML
	private TableColumn<Persona, String> tblApellidos;
	
	@FXML
	private TableColumn<Persona, Integer> tblEdad;
	
	@FXML
	private TableColumn<Persona, String> tblNombre;
	
    @FXML
    private TextField txtFiltrar;
	
	// Variables de clase
	static ObservableList<Persona> listaPersonas;
	static ObservableList<Persona> listaFiltrada;
	static Persona p=new Persona("", "", 0);
	
	/*
	 * Método de inicialización
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		listaPersonas = FXCollections.observableArrayList();
		listaFiltrada = FXCollections.observableArrayList();
		tblNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
		tblApellidos.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));
		tblEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));		
			
		
		tblTabla.setItems(listaFiltrada);		
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
			listaFiltrada.remove(new Persona(sNombreEliminado, sApellidosEliminado, nEdadEliminado));
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
    
    @FXML
    void filtrarTabla(KeyEvent event) {
    	
    	String sFiltro = txtFiltrar.getText(); 
    	
    	// Iterador para la lista y se añade a una lista auxiliar para mostrar en la tabla
    	Iterator<Persona>it = listaPersonas.iterator();
    	listaFiltrada.clear();
    	
    	while(it.hasNext()) {
    		Persona p = it.next();
    		if (p.getNombre().contains(sFiltro)) {
    			listaFiltrada.add(p);
    		}
    	}
    }
    /*
     * Método para exportar los datos de la tabla a un csv 
     */
    @FXML
    void exportarDatos(ActionEvent event) {

    	
    	File f = new File("./Persona.csv");
    	try {
			FileWriter fw = new FileWriter(f);
			Iterator<Persona> it = listaFiltrada.iterator();
			
			fw.write("Nombre,Apellidos,Edad\n");
			// Si no se muestran datos en la tabla, no se podrá exportar
			if (!it.hasNext()) {
				fw.close();
				throw new NullPointerException();
			}
			while(it.hasNext()) {
				Persona p = it.next();
				fw.write(p.toCSV()+"\n");
			}
			fw.close();
			ventanaAlerta("I", "CSV creado correctamente");
		} catch (IOException e) {
			ventanaAlerta("E", "No se pudo crear el archivo csv");
		} catch (NullPointerException e) {
			ventanaAlerta("E", "No se pueden exportar datos sin ningún registro");
		}
    }
    
    /*
     * Método para importar datos
     */
    @FXML
    void importarDatos(ActionEvent event) {
    	try {
    		JFileChooser fichCSV = new JFileChooser();
        	fichCSV.showOpenDialog(null);
        	File fRutaFichero = fichCSV.getSelectedFile();
        	String extension = fRutaFichero.toString().substring(((int)fRutaFichero.toString().length())-3, (int)fRutaFichero.toString().length());
        	if (extension!="csv") {
        		ventanaAlerta("E", "Seleccione un archivo con extensión .csv");
        	}else {
        		
        	}
    		
        	
    	}catch (NullPointerException e) {
    		
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
	
	// para crear la ventana auxiliar
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