package at.qe.skeleton.miscellaneous;

import at.qe.skeleton.model.AccessPoint;
import at.qe.skeleton.model.TemperaStation;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract. See {@linkplain http://www.jqno.nl/equalsverifier/} for more
 * information.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
public class EqualsImplementationTest {

    @Test
    public void testUserEqualsContract() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        TemperaStation temp1 = new TemperaStation();
        TemperaStation temp2 = new TemperaStation();
        temp1.setId("temp1");
        temp2.setId("temp2");
        AccessPoint ap1 = new AccessPoint();
        ap1.setId(UUID.randomUUID());
        AccessPoint ap2 = new AccessPoint();
        ap2.setId(UUID.randomUUID());
        Set<TemperaStation> tempSet = new HashSet<>();
        tempSet.add(temp1);
        Set<TemperaStation> tempSet2 = new HashSet<>();
        tempSet2.add(temp2);
    EqualsVerifier.forClass(Userx.class)
        .withPrefabValues(Userx.class, user1, user2)
        .withPrefabValues(TemperaStation.class, temp1, temp2)
        .withPrefabValues(AccessPoint.class, ap1, ap2)
        .withPrefabValues(Set.class, tempSet, tempSet2)
        .suppress(Warning.STRICT_INHERITANCE, Warning.ALL_FIELDS_SHOULD_BE_USED)
        .verify();
    }

    @Test
    public void testUserRoleEqualsContract() {
        EqualsVerifier.forClass(UserxRole.class).verify();
    }



}