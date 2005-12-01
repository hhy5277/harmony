/* Copyright 1991, 2005 The Apache Software Foundation or its licensors, as applicable
 * 
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
 */

#if !defined(gp_h)
#define gp_h

struct HySigContext;

typedef UDATA (*protected_fn) (void *);
typedef void (*handler_fn) (UDATA gpType, void *gpInfo, void *userData,
                            struct HySigContext * gpContext);
#define HyPrimErrGPF 0
#define HyPrimErrGPFInvalidRead 1
#define HyPrimErrGPFInvalidWrite 2
#define HyPrimErrGPFInvalidInstruction 3
#define HyPrimErrGPFFloat 4

#endif /* gp_h */
