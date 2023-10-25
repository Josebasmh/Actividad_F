package controller;



import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Persona;

public class ActividadBController2 implements Initializable{

	   @FXML
	    private Button btnCancelar;

	    @FXML
	    private Button btnGuardar;

	    @FXML
	    private TextField txtApellidos;

	    @FXML
	    private TextField txtEdad;

	    @FXML
	    private TextField txtNombre;
	    
	    //variables de clase
	    String camposNulos;
	    
	    /*
	     * Método de inicialización.
	     */
	    @Override
		public void initialize(URL arg0, ResourceBundle arg1) {
				if (!ActividadBController.p.getApellidos().isEmpty()) {
					txtNombre.setText(ActividadBController.p.getNombre());
					txtApellidos.setText(ActividadBController.p.getApellidos());
					txtEdad.setText(ActividadBController.p.getEdad()+"");
				}
		}

	    /*
	     * Metodo para cerrar la ventana auxiliar
	     */
	    @FXML
	    void cancelarVentana(ActionEvent event) {
	    	Node node = (Node)event.getSource();
	    	Stage stage = (Stage) node.getScene().getWindow();
	    	stage.close();
	    }

	    /*
		 * Método para agregar personas a la tabla.
		 * Se controla que los campos no pueden ser nulos y que el campo edad sea un número mayor que 1. 
		 */
		@FXML
	    void guardarPersona(ActionEvent event) {
			if (ActividadBController.p.getNombre().equals("")) {
				aniadir();
			}else {
				modificar();
			}
			cancelarVentana(event);
	    }
		
		/*
		 * Métodos auxiliares
		 */

		void aniadir() {
			String camposNulos = "";
			try {
				// Controlar que los parametros se insertan correctamente
				if (txtNombre.getText().equals("")) {camposNulos = "El campo nombre es obligatorio\n";}
				if (txtApellidos.getText().equals("")) {camposNulos += "El campo apellidos es obligatorio\n";}
				if (txtEdad.getText().isEmpty()) {camposNulos += "El campo edad es obligatorio";}
				if (camposNulos!="") {throw new NullPointerException();}
				if (Integer.parseInt(txtEdad.getText().toString()) < 1) {throw new NumberFormatException();}
				
				// Crear persona
				String nombre= txtNombre.getText();
				String apellidos= txtApellidos.getText();
				Integer edad= Integer.parseInt(txtEdad.getText());
				Persona p = new Persona(nombre, apellidos, edad);
				// Insertar persona, controlando que no exista
				if (ActividadBController.listaPersonas.contains(p)== false) {
					ActividadBController.listaPersonas.add(p);
					ActividadBController.ventanaAlerta("I", "Persona añadida correctamente");
					eliminarValores();
				}else{
					ActividadBController.ventanaAlerta("E", "La persona ya existe");
				}	
			}catch(NullPointerException e){
				ActividadBController.ventanaAlerta("E", camposNulos);
			}catch(NumberFormatException e) {
				ActividadBController.ventanaAlerta("E", "El valor de edad debe ser un número mayor que cero");
			}
		}
		
		void modificar() {
			camposNulos="";
	    	try {
	    		// Crear persona para comprobar que no esxiste
	    		Persona pAux = new Persona(txtNombre.getText(), txtApellidos.getText(), Integer.parseInt(txtEdad.getText()));
	    		if (!ActividadBController.listaPersonas.contains(pAux)) {
	        		// Modificar persona
	    			ActividadBController.listaPersonas.remove(ActividadBController.p);
	    			ActividadBController.listaPersonas.add(pAux);
	    			ActividadBController.ventanaAlerta("I", "Persona modificada correctamente");
	    			eliminarValores();
	    		}else {
	    			ActividadBController.ventanaAlerta("E", "Persona existente");
	    		}
	    		
	    	}catch(NullPointerException e){
	    		ActividadBController.ventanaAlerta("E", camposNulos);
	    	}
		}
		// Vacia los editText  
		void eliminarValores() {
			txtNombre.clear();
			txtApellidos.clear();
			txtEdad.clear();
		}
}