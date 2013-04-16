package br.com.caelum.carangos.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.caelum.carangos.gcm.model.Device;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class DeviceDao {

	private EntityManager em;

	public DeviceDao(EntityManager em) {
		this.em = em;
	}

	public void save(Device device) {
		em.persist(device);
	}

	public Device find(String hash) {
		return em.createQuery("from "+Device.class.getName()+ " where registrationId = :id",
				Device.class).setParameter("id", hash)
			.getSingleResult();
	}
	
	public List<String> listAllRegistrationIds() {
		return em.createNativeQuery("select d.registrationId from Device as d").getResultList();
//		return em.createQuery("select registrationId from "+ Device.class.getName(), String.class).getResultList();
	}
	
	public boolean update(String oldHash, String newHash) {
		em.getTransaction().begin();
		Device found = find(oldHash);
		
		if (found != null) {
			found.setRegistrationId(newHash);
			return true;
		}
		em.getTransaction().commit();
		return false;
	}

	public boolean contains(String registrationId) {
		return em.createQuery("select count(d.id) from "+Device.class.getName()+ " as d where d.registrationId = :id",
				Long.class).setParameter("id", registrationId).getSingleResult()>0;
	}
}
