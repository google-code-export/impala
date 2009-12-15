/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tests.integration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.impalaframework.facade.Impala;
import org.impalaframework.interactive.InteractiveTestRunner;
import org.impalaframework.module.ModuleDefinitionSource;
import org.impalaframework.module.RootModuleDefinition;
import org.impalaframework.module.source.SimpleModuleDefinitionSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.util.FileCopyUtils;

/**
 * Base class for Clinic tests. Allows subclasses to specify context locations.
 * 
 * <p>
 * As opposed to the original Spring implementation, this class does not extend
 * AbstractTransactionalDataSourceSpringContextTests. Instead, beans are
 * obtained using <code>DynamicContextHolder.getBean</code>. Otherwise, the
 * contents of the test methods themselves are based on the contents of the
 * original <code>AbstractClinicTests</code> implementation.
 * 
 * @see org.impalaframework.testrun.DynamicContextHolder#getBean(String, Class)
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Phil Zoio
 */
public class HibernateClinicTest extends TestCase implements
        ModuleDefinitionSource {

    public static void main(String[] args) {
        InteractiveTestRunner.run(HibernateClinicTest.class);
    }

    private JdbcTemplate jdbcTemplate;

    private Clinic clinic;

    public void setUp() throws Exception {
        super.setUp();
        Impala.init(this);
        clinic = Impala.getBean("clinic", Clinic.class);
        jdbcTemplate = new JdbcTemplate(Impala.getBean(
                "dataSource", DataSource.class));

        runScript("../petclinic-build/db/emptyDB.txt");
        runScript("../petclinic-build/db/populateDB.txt");
    }

    private void runScript(String file) throws IOException,
            FileNotFoundException {
        String empty = FileCopyUtils.copyToString(new FileReader(file));
        String[] statements = empty.split(";");
        for (String sql : statements) {
            if (sql.trim().length() > 0)
                jdbcTemplate.execute(sql);
        }
    }

    /**
     * This method is provided to set the Clinic instance being tested by the
     * Dependency Injection injection behaviour of the superclass from the
     * <code>org.springframework.test</code> package.
     * 
     * @param clinic
     *            clinic to test
     */
    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }
    
    /* commented out for demo
    @SuppressWarnings("unchecked")
    public void testVetsHaveSurgery() {
        Collection<Vet> vets = this.clinic.getVets();
        for (Vet vet : vets) {
            assertNotNull(vet.getSurgery());
            assertNotNull(vet.getSurgery().getName());
        }
    }
    */
    
    /* commented out for demo
    public void testGetSurgeries() {
        Collection surgeries = this.clinic.getSurgeries();
        assertEquals(3, surgeries.size());
    }*/
    

    public void testGetVets() {
        Collection<?> vets = this.clinic.getVets();

        // Use the inherited JdbcTemplate (from
        // AbstractTransactionalDataSourceSpringContextTests)
        // to verify the results of the query
        assertEquals("JDBC query must show the same number of vets",
                jdbcTemplate.queryForInt("SELECT COUNT(0) FROM vets"), vets
                        .size());
        Vet v1 = (Vet) EntityUtils.getById(vets, Vet.class, 2);
        assertEquals("Leary", v1.getLastName());
        assertEquals(1, v1.getNrOfSpecialties());
        assertEquals("radiology", ((Specialty) v1.getSpecialties().get(0))
                .getName());
        Vet v2 = (Vet) EntityUtils.getById(vets, Vet.class, 3);
        assertEquals("Douglas", v2.getLastName());
        assertEquals(2, v2.getNrOfSpecialties());
        assertEquals("dentistry", ((Specialty) v2.getSpecialties().get(0))
                .getName());
        assertEquals("surgery", ((Specialty) v2.getSpecialties().get(1))
                .getName());
    }

    public void testGetPetTypes() {
        Collection<?> petTypes = this.clinic.getPetTypes();
        assertEquals("JDBC query must show the same number of pet typess",
                jdbcTemplate.queryForInt("SELECT COUNT(0) FROM types"),
                petTypes.size());
        PetType t1 = (PetType) EntityUtils.getById(petTypes, PetType.class, 1);
        assertEquals("cat", t1.getName());
        PetType t4 = (PetType) EntityUtils.getById(petTypes, PetType.class, 4);
        assertEquals("snake", t4.getName());
    }

    public void testFindOwners() {
        Collection<?> owners = this.clinic.findOwners("Davis");
        assertEquals(2, owners.size());
        owners = this.clinic.findOwners("Daviss");
        assertEquals(0, owners.size());
    }

    public void testLoadOwner() {
        Owner o1 = this.clinic.loadOwner(1);
        assertTrue(o1.getLastName().startsWith("Franklin"));
        Owner o10 = this.clinic.loadOwner(10);
        assertEquals("Carlos", o10.getFirstName());
    }

    public void testInsertOwner() {
        Collection<?> owners = this.clinic.findOwners("Schultz");
        int found = owners.size();
        Owner owner = new Owner();
        owner.setLastName("Schultz");
        this.clinic.storeOwner(owner);
        owners = this.clinic.findOwners("Schultz");
        assertEquals(found + 1, owners.size());
    }

    public void testUpdateOwner() throws Exception {
        Owner o1 = this.clinic.loadOwner(1);
        String old = o1.getLastName();
        o1.setLastName(old + "X");
        this.clinic.storeOwner(o1);
        o1 = this.clinic.loadOwner(1);
        assertEquals(old + "X", o1.getLastName());
    }

    public void testLoadPet() {
        Collection<?> types = this.clinic.getPetTypes();
        Pet p7 = this.clinic.loadPet(7);
        assertTrue(p7.getName().startsWith("Samantha"));
        assertEquals(EntityUtils.getById(types, PetType.class, 1).getId(), p7
                .getType().getId());
        assertEquals("Jean", p7.getOwner().getFirstName());
        Pet p6 = this.clinic.loadPet(6);
        assertEquals("George", p6.getName());
        assertEquals(EntityUtils.getById(types, PetType.class, 4).getId(), p6
                .getType().getId());
        assertEquals("Peter", p6.getOwner().getFirstName());
    }

    public void testInsertPet() {
        Owner o6 = this.clinic.loadOwner(6);
        int found = o6.getPets().size();
        Pet pet = new Pet();
        pet.setName("bowser");
        Collection<?> types = this.clinic.getPetTypes();
        pet.setType((PetType) EntityUtils.getById(types, PetType.class, 2));
        pet.setBirthDate(new Date());
        o6.addPet(pet);
        assertEquals(found + 1, o6.getPets().size());
        this.clinic.storeOwner(o6);
        o6 = this.clinic.loadOwner(6);
        assertEquals(found + 1, o6.getPets().size());
    }

    public void testUpdatePet() throws Exception {
        Pet p7 = this.clinic.loadPet(7);
        String old = p7.getName();
        p7.setName(old + "X");
        this.clinic.storePet(p7);
        p7 = this.clinic.loadPet(7);
        assertEquals(old + "X", p7.getName());
    }

    public void testInsertVisit() {
        Pet p7 = this.clinic.loadPet(7);
        int found = p7.getVisits().size();
        Visit visit = new Visit();
        p7.addVisit(visit);
        visit.setDescription("test");
        this.clinic.storeVisit(visit);
        p7 = this.clinic.loadPet(7);
        assertEquals(found + 1, p7.getVisits().size());
    }

    public RootModuleDefinition getModuleDefinition() {

        return new SimpleModuleDefinitionSource("petclinic-main",
                new String[] { "petclinic-context.xml" }, new String[] {
                        "petclinic-hibernate", 
                        "petclinic-service" })
                .getModuleDefinition();
    }

}
