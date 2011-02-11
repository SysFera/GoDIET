
package com.sysfera.godiet.Utils.corba.generated;

/**
* ToolMsgReceiverOperations.java .
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
public interface ToolMsgReceiverOperations 
{

  /**
     * Receive a buffer of messages. The messages are ordered in the
     * sequence. Older messages will be at the beginning of the sequence,
     * younger(newer) messages at the end. 
     */
  void sendMsg (log_msg_t[] msgBuf);
} // interface ToolMsgReceiverOperations
