package com.etc.admin.ui.dynamicfilespecification.employee;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.etc.admin.EtcAdmin;
import com.etc.admin.data.DataManager;
import com.etc.admin.utils.Utils.ScreenType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewDynamicFileSpecificationEmployeeController {
	
	@FXML
	private TableView<ColumnField> dfsempColumnFieldTable;
	@FXML
	private Label empStateLabel;
	@FXML
	private ListView<HBoxCell> empSecondariesListView;
	@FXML
	private Label empCoreIdLabel;
	@FXML
	private Label empCoreActiveLabel;
	@FXML
	private Label empCoreBODateLabel;
	@FXML
	private Label empCoreLastUpdatedLabel;

	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {
		initControls();
		
		loadColumnFields();
				
		//updateEmployeeData();
		//loadSecondaries();
		//loadEmploymentPeriods();
		//loadEmployeeCoveragePeriods();
	}

	private void initControls() {

	    
 		TableColumn<ColumnField,String> nameColumn = new TableColumn<>("Name");
 		nameColumn.setMinWidth(150);
		nameColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("name"));

		TableColumn<ColumnField,String> columnColumn = new TableColumn<ColumnField,String>("Column");
		columnColumn.setMinWidth(150);
		columnColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("column"));

		TableColumn<ColumnField,String> columnIndexColumn = new TableColumn<ColumnField,String>("Column Index");
		columnIndexColumn.setMinWidth(150);
		columnIndexColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("columnIndex"));

		TableColumn<ColumnField,String> parsePatternColumn = new TableColumn<ColumnField,String>("Parse Pattern");
		parsePatternColumn.setMinWidth(150);
		parsePatternColumn.setCellValueFactory(new PropertyValueFactory<ColumnField,String>("parsePattern"));
		
		dfsempColumnFieldTable.getColumns().clear();
		dfsempColumnFieldTable.getColumns().add(nameColumn); 
		dfsempColumnFieldTable.getColumns().add(columnColumn);
		dfsempColumnFieldTable.getColumns().add(columnIndexColumn);
		dfsempColumnFieldTable.getColumns().add(parsePatternColumn);		

	}	
	
	private void loadColumnFields() {
		//add some data
		List<ColumnField> list = new ArrayList<ColumnField>();
		list.add(new ColumnField("First Name","3", "4", "2"));
		list.add(new ColumnField("Middle Name","3", "4", "2"));
		list.add(new ColumnField("Last Name","3", "4", "2"));
		list.add(new ColumnField("Social Security Number","3", "4", "2"));
		list.add(new ColumnField("Employee Identifier","3", "4", "2"));
		list.add(new ColumnField("Street Number","3", "4", "2"));
		list.add(new ColumnField("Street or Address","3", "4", "2"));
		list.add(new ColumnField("Address Line 2","3", "4", "2"));
		list.add(new ColumnField("City","3", "4", "2"));
		list.add(new ColumnField("State","3", "4", "2"));
		list.add(new ColumnField("Zip","3", "4", "2"));
		list.add(new ColumnField("Gender","3", "4", "2"));
		list.add(new ColumnField("Date of Birth","3", "4", "2"));
		list.add(new ColumnField("Hire Date","3", "4", "2"));
		list.add(new ColumnField("Rehire Date","3", "4", "2"));
		list.add(new ColumnField("Termination Date","3", "4", "2"));
		list.add(new ColumnField("Department","3", "4", "2"));
		list.add(new ColumnField("Job Title","3", "4", "2"));
		list.add(new ColumnField("Custom Field 1","3", "4", "2"));
		list.add(new ColumnField("Custom Field 2","3", "4", "2"));
		list.add(new ColumnField("Custom Field 3","3", "4", "2"));
		list.add(new ColumnField("Custom Field 4","3", "4", "2"));
		list.add(new ColumnField("Custom Field 5","3", "4", "2"));

        ObservableList<ColumnField> myObservableList = FXCollections.observableList(list);
        dfsempColumnFieldTable.setItems(myObservableList);
		
	}

	private void updateEmployeeData(){

		
/*		if (EtcAdmin.mEmployee != null) {
			String sName;
			sName = EtcAdmin.mEmployee.getFirstName() + " " + EtcAdmin.mEmployee.getLastName();
			Utils.updateControl(empNameLabel,sName);
			Utils.updateControl(empFirstNameLabel,EtcAdmin.mEmployee.getFirstName());
			Utils.updateControl(empMiddleNameLabel,EtcAdmin.mEmployee.getMiddleName());
			Utils.updateControl(empLastNameLabel,EtcAdmin.mEmployee.getLastName());
			Utils.updateControl(empEIDLabel,EtcAdmin.mEmployee.getEmployeeIdentifier());

			if (EtcAdmin.mEmployee.getGenderType() != null) 
				Utils.updateControl(empGenderLabel,EtcAdmin.mEmployee.getGenderType().toString());
			else
				empGenderLabel.setText("");

			Utils.setControlDate(empDOBLabel, EtcAdmin.mEmployee.getDateOfBirth());
			Utils.setControlDate(empHireDateLabel, EtcAdmin.mEmployee.getHireDate());
			Utils.setControlDate(empTermDateLabel, EtcAdmin.mEmployee.getTerminationDate());

			if (EtcAdmin.mEmployee.getEmploymentStatusType() != null) 
				Utils.updateControl(empEmpStatusLabel,EtcAdmin.mEmployee.getEmploymentStatusType().toString());
			else
				empEmpStatusLabel.setText("");

			Utils.updateControl(empJobTitleLabel,EtcAdmin.mEmployee.getJobTitle());

			if (EtcAdmin.mEmployee.getDepartment() != null) 
				Utils.updateControl(empDepartmentLabel,EtcAdmin.mEmployee.getDepartment().getName());
			else
				empDepartmentLabel.setText("");
			
			Utils.updateControl(empStreetLabel,EtcAdmin.mEmployee.getMailStreet());
			Utils.updateControl(empCityLabel,EtcAdmin.mEmployee.getMailCity());
			Utils.updateControl(empStateLabel,EtcAdmin.mEmployee.getMailState());
			Utils.updateControl(empZipLabel,EtcAdmin.mEmployee.getMailZip());
			
			//core data read only
			Utils.updateControl(empCoreIdLabel,String.valueOf(EtcAdmin.mEmployee.getId()));
			Utils.updateControl(empCoreActiveLabel,String.valueOf(EtcAdmin.mEmployee.isActive()));
			Utils.setControlDate(empCoreBODateLabel,EtcAdmin.mEmployee.getBornOn());
			Utils.setControlDate(empCoreLastUpdatedLabel,EtcAdmin.mEmployee.getLastUpdated());
			
		}
	*/
	}
	
	private void loadSecondaries() {
		
/*		//update the secondaries list
		String sAddress;
		String sName;
		
		if (EtcAdmin.mSecondaries != null) {
				List<HBoxCell> list = new ArrayList<>();
				list.add(new HBoxCell("Name","Address", null, 0));
				
				int i = 0;
				for (Secondary secondary : EtcAdmin.mSecondaries) {
					// create the employee address string for display
					sAddress = secondary.getMailStreet() + " " + 
							secondary.getMailCity() + ", " +
							secondary.getMailState() + " " +
							secondary.getMailZip();
					
					//create a full name for the list
					sName = secondary.getFirstName() + " " + secondary.getLastName(); 
					list.add(new HBoxCell(sName, sAddress, "View",i));
					i++;
				};	
				
		        ObservableList<HBoxCell> myObservableList = FXCollections.observableList(list);
		   //     empSecondariesListView.setItems(myObservableList);		
				
		        empSecondariesLabel.setText("Secondaries (total: " + String.valueOf(EtcAdmin.mSecondaries.size()) + ")" );
			} else {
				empSecondariesLabel.setText("Secondaries (total: 0)");
			}
*/	}
	
	//update the employment periods list
	private void loadEmploymentPeriods() {
	    
/*		if (EtcAdmin.mEmployee.getEmploymentPeriods() != null)
		{
		    List<HBoxEmploymentPeriodCell> employmentPeriodList = new ArrayList<HBoxEmploymentPeriodCell>();
			
		    employmentPeriodList.add(new HBoxEmploymentPeriodCell("Hire Date", "Termination Date", null, 0));
			
		    int i = 0;
		    for (EmploymentPeriod employmentPeriod : EtcAdmin.mEmployee.getEmploymentPeriods()) {
				if (employmentPeriod == null) continue;
				
				String sHireDate = "";
				if (employmentPeriod.getHireDate() != null)
					sHireDate = sdf.format(employmentPeriod.getHireDate().getTime());
				
				String sTerminationDate = "";
				if (employmentPeriod.getTermDate() != null)
					sTerminationDate = sdf.format(employmentPeriod.getTermDate().getTime());

				employmentPeriodList.add(new HBoxEmploymentPeriodCell(sHireDate, sTerminationDate, "View", i));
				i++;
		    };	
			
			ObservableList<HBoxEmploymentPeriodCell> myObservableEmploymentPeriodList = FXCollections.observableList(employmentPeriodList);
			empEmploymentPeriodsListView.setItems(myObservableEmploymentPeriodList);		
			
			//update our employer screen label
			empEmploymentPeriodsLabel.setText("Employment Periods (total: " + String.valueOf(EtcAdmin.mEmployee.getEmploymentPeriods().size()) + ")");
		} else {
			empEmploymentPeriodsLabel.setText("Employment Periods (total: 0)");			
		}
*/	}
	
	//update the employment periods list
	private void loadEmployeeCoveragePeriods() {
	    
/*		if (EtcAdmin.mEmployee.getEmployeeCoveragePeriods() != null)
		{
		    List<HBoxEmployeeCoveragePeriodCell> employeeCoveragePeriodList = new ArrayList<HBoxEmployeeCoveragePeriodCell>();
			
		    employeeCoveragePeriodList.add(new HBoxEmployeeCoveragePeriodCell("Start Date", "End Date", "Waived", false,"Ineligible", false, null, 0));
			
		    int i = 0;
		    for (EmployeeCoveragePeriod employeeCoveragePeriod : EtcAdmin.mEmployee.getEmployeeCoveragePeriods()) {
				if (employeeCoveragePeriod == null) continue;
				
				String sStartDate = "";
				if (employeeCoveragePeriod.getStartDate() != null)
					sStartDate = sdf.format(employeeCoveragePeriod.getStartDate().getTime());
				
				String sEndDate = "";
				if (employeeCoveragePeriod.getEndDate() != null)
					sEndDate = sdf.format(employeeCoveragePeriod.getEndDate().getTime());

				employeeCoveragePeriodList.add(new HBoxEmployeeCoveragePeriodCell(sStartDate, 
																				  sEndDate, 
																				  null,
																				  employeeCoveragePeriod.isWaived(),
																				  null,
																				  employeeCoveragePeriod.isIneligible(),
																				  "View", i));
				i++;
			};	
			
			ObservableList<HBoxEmployeeCoveragePeriodCell> myObservableEmployeeCoveragePeriodList = FXCollections.observableList(employeeCoveragePeriodList);
			empEmployeeCoveragePeriodsListView.setItems(myObservableEmployeeCoveragePeriodList);		
			
			//update our employer screen label
			empEmployeeCoveragePeriodsLabel.setText("Employee Coverage Periods (total: " + String.valueOf(EtcAdmin.mEmployee.getEmployeeCoveragePeriods().size()) + ")");
		} else {
			empEmployeeCoveragePeriodsLabel.setText("Employee Coverage Periods (total: 0)");			
		}
*/	}
	
	public class ColumnField {
	    private String name;
	    private String column;
	    private String columnIndex;
	    private String parsePattern;

	    public ColumnField(String name, String column, String columnIndex, String parsePattern) {
	        this.name = name;
	        this.column = column;
	        this.columnIndex = columnIndex;
	        this.parsePattern = parsePattern;
	    }

	    public String getName() {
	        return name;
	    }

	    public String getColumn() {
	        return column;
	    }

	    public String getColumnIndex() {
	        return columnIndex;
	    }

	    public String getParsePattern() {
	        return parsePattern;
	    }
	}
	
	//extending the listview for our additional controls
	public static class HBoxCell extends HBox {
         Label lblName = new Label();
         Label lblAddress = new Label();
         Button btnView = new Button();

         HBoxCell(String sName, String sAddress, String sButtonText, int nButtonID) {
              super();

              if (sName == null) sName = "";             
              if (sAddress == null) sAddress = "";
              
              if (sName.contains("null")) sName = "";
              if (sAddress.contains("null")) sAddress = "";
              
              // check to see if we have a bad address from all nulls
              if (sAddress.equals(" ,  "))
            	  sAddress = "";
              
              lblName.setText(sName);
              lblName.setMinWidth(280);
              lblName.setMaxWidth(280);
              lblName.setPrefWidth(280);
              HBox.setHgrow(lblName, Priority.ALWAYS);

              lblAddress.setText(sAddress);
              lblAddress.setMinWidth(390);
              lblAddress.setMaxWidth(390);
              lblAddress.setPrefWidth(390);
              HBox.setHgrow(lblAddress, Priority.ALWAYS);

              if (sButtonText != null) {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
              }       
              
              if (sButtonText == null) {
            	  lblName.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblAddress.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblName, lblAddress);
              }else {
            	  this.getChildren().addAll(lblName, lblAddress, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current secondary
            			  //DataManager.i().setSecondary(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	
	
	//extending the listview for our additional controls
	public static class HBoxEmploymentPeriodCell extends HBox {
         Label lblHireDate = new Label();
         Label lblTerminationDate = new Label();
         Button btnView = new Button();

         HBoxEmploymentPeriodCell(String sHireDate, String sTerminationDate, String sButtonText, int nButtonID) {
              super();

              if (sHireDate == null)  sHireDate = "";
              if (sTerminationDate == null)  sTerminationDate = "";
              
              if (sHireDate.contains("null")) sHireDate = "";
              if (sTerminationDate.contains("null")) sTerminationDate = "";
              
              lblHireDate.setText(sHireDate);
              lblHireDate.setMinWidth(280);
              lblHireDate.setMaxWidth(280);
              lblHireDate.setPrefWidth(280);
              HBox.setHgrow(lblHireDate, Priority.ALWAYS);

              lblTerminationDate.setText(sTerminationDate);
              lblTerminationDate.setMinWidth(390);
              lblTerminationDate.setMaxWidth(390);
              lblTerminationDate.setPrefWidth(390);
              HBox.setHgrow(lblTerminationDate, Priority.ALWAYS);

              if (sButtonText != null) {
            	  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));
              }       
              
              if (sButtonText == null) {
            	  lblHireDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTerminationDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  this.getChildren().addAll(lblHireDate, lblTerminationDate);
              }else {
            	  this.getChildren().addAll(lblHireDate, lblTerminationDate, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employment period
            			  DataManager.i().setEmploymentPeriod(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	

	//extending the listview for our additional controls
	public static class HBoxEmployeeCoveragePeriodCell extends HBox {
         Label lblStartDate = new Label();
         Label lblEndDate = new Label();
         Label lblTitle1 = new Label();
         Label lblTitle2 = new Label();
         CheckBox cbWaived = new CheckBox();
         CheckBox cbIneligible = new CheckBox();
         Button btnView = new Button();

         HBoxEmployeeCoveragePeriodCell(String sStartDate, String sEndDate, String sTitle1, Boolean bWaived, String sTitle2, Boolean bIneligible, String sButtonText, int nButtonID) {
              super();

              if (sStartDate == null)  sStartDate = "";              
              if (sEndDate == null)  sEndDate = "";
              if (sTitle1 == null)  sTitle1 = "";
              if (sTitle2 == null)  sTitle2 = "";
              
              if (sStartDate.contains("null")) sStartDate = "";
              if (sEndDate.contains("null")) sEndDate = "";
              if (sTitle1.contains("null")) sTitle1 = "";
              if (sTitle2.contains("null")) sTitle2 = "";
              
              lblStartDate.setText(sStartDate);
              lblStartDate.setMinWidth(150);
              lblStartDate.setMaxWidth(150);
              lblStartDate.setPrefWidth(150);
              HBox.setHgrow(lblStartDate, Priority.ALWAYS);

              lblEndDate.setText(sEndDate);
              lblEndDate.setMinWidth(150);
              lblEndDate.setMaxWidth(150);
              lblEndDate.setPrefWidth(150);
              HBox.setHgrow(lblEndDate, Priority.ALWAYS);

              //if the button text is null, then we'll call it a header row and add out titles
              if (sButtonText == null) {

            	  lblStartDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblEndDate.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle1.setFont(Font.font(null, FontWeight.BOLD, 13));
            	  lblTitle2.setFont(Font.font(null, FontWeight.BOLD, 13));

            	  lblTitle1.setText(sTitle1);
                  lblTitle1.setMinWidth(185);
                  lblTitle1.setMaxWidth(185);
                  lblTitle1.setPrefWidth(185);

                  lblTitle2.setText(sTitle2);
                  lblTitle2.setMinWidth(185);
                  lblTitle2.setMaxWidth(185);
                  lblTitle2.setPrefWidth(185);
                  
            	  this.getChildren().addAll(lblStartDate, lblEndDate, lblTitle1, lblTitle2);                  
              }
              else {

                  cbWaived.setText("");
                  cbWaived.setMinWidth(185);
                  cbWaived.setMaxWidth(185);
                  cbWaived.setPrefWidth(185);
                  cbWaived.setDisable(true);
                  cbWaived.setSelected(bWaived);

                  cbIneligible.setText("");
                  cbIneligible.setMinWidth(185);
                  cbIneligible.setMaxWidth(185);
                  cbIneligible.setPrefWidth(185);
                  cbIneligible.setDisable(true);
                  cbIneligible.setSelected(bIneligible);

                  btnView.setText(sButtonText);
            	  btnView.setId(String.valueOf(nButtonID));

            	  this.getChildren().addAll(lblStartDate, lblEndDate, cbWaived, cbIneligible, btnView);
            	  
            	  //add an event handler for this button
            	  btnView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            		  
            		  @Override
            		  public void handle(MouseEvent event) {
            			  // let the application load the current employment period
            			  //DataManager.i().setEmployeeCoveragePeriod(Integer.parseInt(btnView.getId()));
            		  }
            	  });
              }
              
         }
    }	

	@FXML
	private void onEdit(ActionEvent event) {
		EtcAdmin.i().setScreen(ScreenType.EMPLOYEEEDIT);
	}	
	
/*	public static class Employee {
		 
        private final SimpleStringProperty name;
        private final SimpleStringProperty department;
 
        private Employee(String name, String department) {
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
        }
 
        public String getName() {
            return name.get();
        }
 
        public void setName(String fName) {
            name.set(fName);
        }
 
        public String getDepartment() {
            return department.get();
        }
 
        public void setDepartment(String fName) {
            department.set(fName);
        }
    }	
	*/	
}


