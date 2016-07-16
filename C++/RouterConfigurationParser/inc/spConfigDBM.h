/* 
 * File:   spDBMForConfig.h
 * Author: root
 *
 * Created on February 13, 2015, 3:11 PM
 */

#ifndef SPDBMFORCONFIG_H
#define	SPDBMFORCONFIG_H

#include<spBaseTree.h>
#include<spCiscoParser.h>
#include "mysql.h"

class spDBMForConfig 
{
    string m_DBName;
    string m_password;
    string m_userName;
    int m_ctr;

    public:
      spDBMForConfig(string, string, string);
      int getLastUsedId(string);
      void WriteSchemaTreeToDB(CBaseTree *);
      void WriteSchemaTreeToDB(CBaseTree *, MYSQL*);
      void WriteValueTreeToDB(CBaseTree *);
      void WriteValueTreeToDB(CBaseTree *, MYSQL*);
      CBaseTree* ReadSchemaTreeFromDB();
      CBaseTree* ReadValueTreeFromDB(CBaseTree *);
      MYSQL* ConnectToDB();
      void DisconnectDB( MYSQL*);
      ~spDBMForConfig();
};




#endif	/* SPDBMFORCONFIG_H */

