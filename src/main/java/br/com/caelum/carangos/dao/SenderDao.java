package br.com.caelum.carangos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.caelum.carangos.gcm.model.Device;
import br.com.caelum.carangos.gcm.model.Sender;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class SenderDao {
	private EntityManager em;

	public SenderDao(EntityManager em) {
		this.em = em;
	}

	public Sender findOrCreateSenderForEmail(String email) {
		try {
			return  em.createQuery(
							"from " + Sender.class.getName()
									+ " where email = :email", Sender.class)
					.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			Sender sender = new Sender(email, null);
			em.persist(sender);
			
			return sender;
			
		}
	}

	public List<Device> findDevicesOf(Sender sender) {
		return  em.createQuery(
				"from " + Device.class.getName()
						+ " where sender = :sender", Device.class)
		.setParameter("sender", sender).getResultList();
	}

	public Sender findByEmail(String email) {
		try {
			return  em.createQuery(
							"from " + Sender.class.getName()
									+ " where email = :email", Sender.class)
					.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
