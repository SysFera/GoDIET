
package com.sysfera.godiet.diet.corba.generated;

/**
* MaDagHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from /home/phi/Soft/GoDIET/goDiet/diet/corba/idl/MaDag.idl
* mercredi 26 janvier 2011 17 h 07 CET
*/

abstract public class MaDagHelper
{
  private static String  _id = "IDL:MaDag:1.0";

  public static void insert (org.omg.CORBA.Any a, MaDag that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static MaDag extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (MaDagHelper.id (), "MaDag");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static MaDag read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_MaDagStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, MaDag value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static MaDag narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof MaDag)
      return (MaDag)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      _MaDagStub stub = new _MaDagStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static MaDag unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof MaDag)
      return (MaDag)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      _MaDagStub stub = new _MaDagStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
