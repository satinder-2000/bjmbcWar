/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.State;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
@Stateless
public class StateEjb implements StateEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(StateEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;

    @Override
    public List<State> getAllStates() {
        TypedQuery<State> tQ = em.createQuery("select s from State s", State.class);
        List<State> states = tQ.getResultList();
        LOGGER.info(String.format("Total States are %d", states.size()));
        return states;
        
    }

    @Override
    public State getByCode(String code) {
        TypedQuery<State> tQ = em.createQuery("select s from State s where s.code =?1", State.class);
        tQ.setParameter(1, code);
        return tQ.getSingleResult();
    }

}
