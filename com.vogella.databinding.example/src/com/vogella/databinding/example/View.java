package com.vogella.databinding.example;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.swt.WidgetProperties;

import com.vogella.databinding.example.model.*;


public class View extends ViewPart {
	public static final String ID = "com.vogella.databinding.example.view";

	private TableViewer viewer;
	
	private Person person;

	private Text firstName;
	private Text ageText;
	private Button marriedButton;
	private Combo genderCombo;
	private Text countryText;

	private Person createPerson() {
		Person person = new Person();
		Address address = new Address();
		address.setCountry("Deutschland");
		person.setAddress(address);
		person.setFirstName("John");
		person.setLastName("Doo");
		person.setGender("Male");
		person.setAge(12);
		person.setMarried(true);
		return person;
	}

	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		person = createPerson();
		
	    GridLayout layout = new GridLayout(2, false);
	    layout.marginRight = 5;
	    parent.setLayout(layout);
	    
	    Label firstLabel = new Label(parent, SWT.NONE);
	    firstLabel.setText("Firstname: ");
	    firstName = new Text(parent, SWT.BORDER);

	    GridData gridData = new GridData();
	    gridData.horizontalAlignment = SWT.FILL;
	    gridData.grabExcessHorizontalSpace = true;
	    firstName.setLayoutData(gridData);
	    
	    Label ageLabel = new Label(parent, SWT.NONE);
	    ageLabel.setText("Age: ");
	    ageText = new Text(parent, SWT.BORDER);
	    
	    gridData = new GridData();
	    gridData.horizontalAlignment = SWT.FILL;
	    gridData.grabExcessHorizontalSpace = true;
	    ageText.setLayoutData(gridData);
	    
	    Label marriedLabel = new Label(parent, SWT.NONE);
	    marriedLabel.setText("Married: ");
	    marriedButton = new Button(parent, SWT.CHECK);

	    Label genderLabel = new Label(parent, SWT.NONE);
	    genderLabel.setText("Gender: ");
	    genderCombo = new Combo(parent, SWT.NONE);
	    genderCombo.add("Male");
	    genderCombo.add("Female");

	    Label countryLabel = new Label(parent, SWT.NONE);
	    countryLabel.setText("Country");
	    countryText = new Text(parent, SWT.BORDER);
	    
	    Button button1 = new Button(parent, SWT.PUSH);
	    button1.setText("Write model");
	    button1.addSelectionListener(new SelectionAdapter() {

	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        System.out.println("Firstname: " + person.getFirstName());
	        System.out.println("Age " + person.getAge());
	        System.out.println("Married: " + person.isMarried());
	        System.out.println("Gender: " + person.getGender());
	        System.out.println("Country: "
	            + person.getAddress().getCountry());
	      }
	    });	 
	    
	    Button button2 = new Button(parent, SWT.PUSH);
	    button2.setText("Change model");
	    button2.addSelectionListener(new SelectionAdapter() {
	      @Override
	      public void widgetSelected(SelectionEvent e) {
	        person.setFirstName("Lars");
	        person.setAge(person.getAge() + 1);
	        person.setMarried(!person.isMarried());
	        if (person.getGender().equals("Male")) {
	          person.setGender("Male");
	        } else {
	          person.setGender("Female");
	        }
	        if (person.getAddress().getCountry().equals("Deutschland")) {
	          person.getAddress().setCountry("USA");
	        } else {
	          person.getAddress().setCountry("Deutschland");
	        }
	      }
	    });
	    
	    bindValues();
	    
//		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
//				| SWT.V_SCROLL);
//		viewer.setContentProvider(new ViewContentProvider());
//		viewer.setLabelProvider(new ViewLabelProvider());
//		// Provide the input to the ContentProvider
//		viewer.setInput(new String[] {"One", "Two", "Three"});
	}

	private void bindValues() {
	    // The DataBindingContext object will manage the databindings
	    // Lets bind it
	    DataBindingContext ctx = new DataBindingContext();
	    IObservableValue widgetValue = WidgetProperties.text(SWT.Modify)
	        .observe(firstName);
	    IObservableValue modelValue = BeanProperties.value(Person.class,
	        "firstName").observe(person);
	    ctx.bindValue(widgetValue, modelValue);

	    // Bind the age including a validator
	    widgetValue = WidgetProperties.text(SWT.Modify).observe(ageText);
	    modelValue = BeanProperties.value(Person.class, "age").observe(person);
	    // add an validator so that age can only be a number
	    IValidator validator = new IValidator() {
	      @Override
	      public IStatus validate(Object value) {
	        if (value instanceof Integer) {
	          String s = String.valueOf(value);
	          if (s.matches("\\d*")) {
	            return ValidationStatus.ok();
	          }
	        }
	        return ValidationStatus.error("Not a number");
	      }
	    };

	    UpdateValueStrategy strategy = new UpdateValueStrategy();
	    strategy.setBeforeSetValidator(validator);

	    Binding bindValue = ctx.bindValue(widgetValue, modelValue, strategy,
	        null);
	    // add some decorations
	    ControlDecorationSupport.create(bindValue, SWT.TOP | SWT.LEFT);

	    widgetValue = WidgetProperties.selection().observe(marriedButton);
	    modelValue = BeanProperties.value(Person.class, "married").observe(person);
	    ctx.bindValue(widgetValue, modelValue);

	    widgetValue = WidgetProperties.selection().observe(genderCombo);
	    modelValue = BeanProperties.value("gender").observe(person);

	    ctx.bindValue(widgetValue, modelValue);

	    // address field is bound to the Ui
	    widgetValue = WidgetProperties.text(SWT.Modify).observe(countryText);

	    modelValue = BeanProperties.value(Person.class, "address.country")
	        .observe(person);
	    ctx.bindValue(widgetValue, modelValue);

	  }	
	

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
//		viewer.getControl().setFocus();
	}
}