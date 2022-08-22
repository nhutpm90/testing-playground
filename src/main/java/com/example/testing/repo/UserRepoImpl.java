package com.example.testing.repo;

import java.util.HashMap;
import java.util.Map;

import com.example.testing.model.User;

public class UserRepoImpl implements UserRepo {

	private Map<String, User> users = new HashMap<>();

	@Override
	public boolean save(User user) {
		boolean result = false;
		String id = user.getId();
		if (!users.containsKey(id)) {
			users.put(id, user);
			result = true;
		}
		return result;
	}
}

