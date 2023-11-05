package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.User;

/**
 * UserRepository is used to provide CRUD operations for the User model. Spring
 * will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user object with the provided name. Spring will generate code to
     * make this happen.
     *
     * @param id
     *            id of the user
     * @return User Found user, null if none.
     */

    @Override
    Optional<User> findById ( Long id );

}
