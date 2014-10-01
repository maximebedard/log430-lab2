package ca.etsmtl.log430.lab2;

import ca.etsmtl.log430.common.Displays;
import ca.etsmtl.log430.common.Menus;
import ca.etsmtl.log430.common.Project;
import ca.etsmtl.log430.common.Resource;

import java.util.Observable;

/**
 * User: maximebedard
 * Date: 2014-10-01
 * Time: 11:06 AM
 */
public class ListPreviousAndCurrentlyAssignedRoles extends Communication {

    /**
     * The constructor must receive a registration number and a component name.
     * This is a one-to-one mapping that is designed to ease the use of the
     * virtual bus. Rather than have to remember numbers, components can be
     * referenced by name. Declared names of the components are used, but you
     * don't have to (keep in mind that this is the declared name, not the class
     * type).
     *
     * @param registrationNumber component registration number
     * @param componentName
     */
    public ListPreviousAndCurrentlyAssignedRoles(Integer registrationNumber, String componentName) {
        super(registrationNumber, componentName);
    }

    /**
     * The update() method is an abstract method that is called whenever the
     * notifyObservers() method is called by the Observable class. First we
     * check to see if the NotificationNumber is equal to this thread's
     * RegistrationNumber. If it is, then we execute.
     *
     * @see ca.etsmtl.log430.lab2.Communication#update(java.util.Observable,
     *      java.lang.Object)
     */
    public void update(Observable thing, Object notificationNumber) {
        Menus menu = new Menus();
        Displays display = new Displays();
        Project project;

        if (registrationNumber.compareTo((Integer) notificationNumber) == 0) {
			/*
			 * First we use a Displays object to list all of the resources. Then
			 * we ask the user to pick a resource using a Menus object.
			 */
            addToReceiverList("ListProjectsComponent");
            signalReceivers("ListProjectsComponent");
            project = menu.pickProject(CommonData.theListOfProjects.getListOfProjects());

			/*
			 * If the user selected an invalid resource, then a message is
			 * printed to the terminal.
			 */
            if (project != null) {
                display.displayRolesOfProject(project, CommonData.theListOfResources.getListOfResources());
            } else {
                System.out.println("\n\n *** Project not found ***");
            }
        }
        removeFromReceiverList("ListResourcesComponent");
    }


}
