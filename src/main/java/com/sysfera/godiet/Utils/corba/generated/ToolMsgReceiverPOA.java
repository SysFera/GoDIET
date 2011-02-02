
package com.sysfera.godiet.Utils.corba.generated;

/**
* ToolMsgReceiverPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ./goDiet/Utils/CORBA/idl/LogTool.idl
* mercredi 26 janvier 2011 17 h 07 CET
*/


/**
 * define callback functions the tool has to implement so 
 * that the monitor can actively forward messages to the
 * tool. Active MessagePulling from the tool is not possible
 * in the moment.
 */
public abstract class ToolMsgReceiverPOA extends org.omg.PortableServer.Servant
 implements ToolMsgReceiverOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("sendMsg", new java.lang.Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {

  /**
     * Receive a buffer of messages. The messages are ordered in the
     * sequence. Older messages will be at the beginning of the sequence,
     * younger(newer) messages at the end. 
     */
       case 0:  // ToolMsgReceiver/sendMsg
       {
         log_msg_t msgBuf[] = log_msg_buf_tHelper.read (in);
         this.sendMsg (msgBuf);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:ToolMsgReceiver:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public ToolMsgReceiver _this() 
  {
    return ToolMsgReceiverHelper.narrow(
    super._this_object());
  }

  public ToolMsgReceiver _this(org.omg.CORBA.ORB orb) 
  {
    return ToolMsgReceiverHelper.narrow(
    super._this_object(orb));
  }


} // class ToolMsgReceiverPOA
