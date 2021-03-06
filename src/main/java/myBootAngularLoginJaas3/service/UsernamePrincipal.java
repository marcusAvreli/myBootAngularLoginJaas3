package myBootAngularLoginJaas3.service;


/*-
 * #%L
 * thinkbig-security-api
 * %%
 * Copyright (C) 2017 ThinkBig Analytics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Basic principal representing a user name.  All authenticated users should be
 * assigned this principal when logged in.
 */
public class UsernamePrincipal extends BasePrincipal {

    private static final long serialVersionUID = 1L;

    public UsernamePrincipal() {
    }

    public UsernamePrincipal(String name) {
        super(name);
    }

}
