/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package bjm.bc.ejb;

import bjm.bc.model.District;
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
public class DistrictEjb implements DistrictEjbLocal {
    
    private static Logger LOGGER = Logger.getLogger(DistrictEjb.class.getName());
    
    @PersistenceContext(name = "bjmbcPU")
    private EntityManager em;

    @Override
    public List<District> getDistrictsOfState(String stateCode) {
        TypedQuery<District> tQ = em.createQuery("select d from District d where d.stateCode=?1", District.class);
        tQ.setParameter(1, stateCode);
        List<District> districts = tQ.getResultList();
        LOGGER.info(String.format("Number of Districts in State {1} are {2}", stateCode, districts.size()));
        return districts;
    }

    @Override
    public District getDistrictById(int id) {
        return em.find(District.class, id);
    }
}
