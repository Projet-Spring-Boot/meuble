package com.spring.social.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.spring.social.entity.UserConnection;

@Repository
@Transactional
public class UserConnectionDAO {

	@Autowired
	private EntityManager entityManager;

	public UserConnection findUserConnectionByUserName(String userName) {

		try {
			String sql = "Select e from " + UserConnection.class.getName() + " e " //
					+ " Where e.userId = :userId ";

			Query query = entityManager.createQuery(sql, UserConnection.class);
			query.setParameter("userId", userName);

			List<UserConnection> list = query.getResultList();

			return list.isEmpty() ? null : list.get(0);
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public void deleteConnectionByUserName(String userName) {
		try {
			String sql = "delete from " + UserConnection.class.getName() + " e " //
					+ " Where e.userId = :userId ";

			Query query = entityManager.createQuery(sql);
			query.setParameter("userId", userName);
			
			query.executeUpdate();


		} catch (NoResultException e) {
			e.printStackTrace();
		}
	}
}