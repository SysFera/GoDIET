/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.40
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.sysfera.vishnu.api.ims.internal;

public class ListProcesses extends EObject {
  private long swigCPtr;

  protected ListProcesses(long cPtr, boolean cMemoryOwn) {
    super(VISHNU_IMSJNI.SWIGListProcessesUpcast(cPtr), cMemoryOwn);
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ListProcesses obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        VISHNU_IMSJNI.delete_ListProcesses(swigCPtr);
      }
      swigCPtr = 0;
    }
    super.delete();
  }

  public ListProcesses() {
   this(VISHNU_IMSJNI.new_ListProcesses(), true);
  }

  public void _initialize() {
    VISHNU_IMSJNI.ListProcesses__initialize(swigCPtr, this);
  }

  public EProcessesList getProcess() {
    return new EProcessesList(VISHNU_IMSJNI.ListProcesses_getProcess(swigCPtr, this), false);
  }

  public SWIGTYPE_p_ecorecpp__mapping__any eGet(int _featureID, boolean _resolve) {
    return new SWIGTYPE_p_ecorecpp__mapping__any(VISHNU_IMSJNI.ListProcesses_eGet(swigCPtr, this, _featureID, _resolve), true);
  }

  public void eSet(int _featureID, SWIGTYPE_p_ecorecpp__mapping__any _newValue) {
    VISHNU_IMSJNI.ListProcesses_eSet(swigCPtr, this, _featureID, SWIGTYPE_p_ecorecpp__mapping__any.getCPtr(_newValue));
  }

  public boolean eIsSet(int _featureID) {
    return VISHNU_IMSJNI.ListProcesses_eIsSet(swigCPtr, this, _featureID);
  }

  public void eUnset(int _featureID) {
    VISHNU_IMSJNI.ListProcesses_eUnset(swigCPtr, this, _featureID);
  }

  public SWIGTYPE_p_ecore__EClass _eClass() {
    long cPtr = VISHNU_IMSJNI.ListProcesses__eClass(swigCPtr, this);
    return (cPtr == 0) ? null : new SWIGTYPE_p_ecore__EClass(cPtr, false);
  }

}