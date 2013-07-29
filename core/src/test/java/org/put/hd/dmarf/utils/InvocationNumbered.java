package org.put.hd.dmarf.utils;

import java.util.List;

import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

/**
 * Utility class for the exact order of invocation using @{link {@link Mockito}
 * 
 * @author Piotr
 * 
 */
public class InvocationNumbered implements VerificationMode {

	private final int number;

	public InvocationNumbered(int number) {
		this.number = number;
	}

	public void verify(VerificationData data) {
		List<Invocation> invocations = data.getAllInvocations();
		InvocationMatcher wanted = data.getWanted();

		if (number >= invocations.size())
			throw new MockitoException(
					"No so many methods have been invoked. Expected: "
					+ number
					+ " total count is: "
					+ invocations.size());

		Invocation targetedInvocation = invocations.get(number);

		if (!wanted.matches(targetedInvocation))
			throw new MockitoException(
					"The invocation is not in the proper place. Expected: "
							+ number);

	}

}
