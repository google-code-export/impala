/*
 * Copyright 2007-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.spring.dynamic.impl;

import org.impalaframework.spring.dynamic.impl.CommunicationMethod;
import org.impalaframework.spring.dynamic.impl.MovementMethod;
import org.impalaframework.spring.dynamic.impl.Person;

public class PersonImpl implements Person {
	private CommunicationMethod communicationMethod;

	private MovementMethod movementMethod;

	public void setCommunicationMethod(CommunicationMethod communicationMethod) {
		this.communicationMethod = communicationMethod;
	}

	public void setMovementMethod(MovementMethod movementMethod) {
		this.movementMethod = movementMethod;
	}

	public void act() {
		communicationMethod.communicate();
		movementMethod.move();
	}
}
