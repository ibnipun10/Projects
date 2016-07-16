#include "spConfigDBM.h"
#include <iostream>
#include <stdlib.h>
#include "spConstants.h"
#include "spUtilities.h"
#include "spBaseTreeManager.h"
#include <sstream>
#include <queue>

spDBMForConfig::spDBMForConfig(string strDBMName, string userName, string password)
{
    m_DBName = strDBMName;
    m_userName = userName;
    m_password = password;

    mysql_library_init(0, NULL, NULL);
}

spDBMForConfig::~spDBMForConfig()
{
    mysql_library_end();
}

/*
 * Description : Get the last used node id from the table
 * @tableName : Name of the table
 */
int spDBMForConfig::getLastUsedId(string tableName)
{
    MYSQL *conn = ConnectToDB();
    int imaxId = 0;

    string query = "select Max(NodeID) from " + tableName;

    if(mysql_real_query(conn, query.c_str(), query.size()))
    {
        cout << "Error executing query " << query;
        return 0;
    }

    MYSQL_RES* result = mysql_store_result(conn);

    MYSQL_ROW row;
    if ((row = mysql_fetch_row(result)))
    {
        if(row[0] != NULL)
            imaxId = atoi(row[0]);
    }

    DisconnectDB(conn);

    return imaxId;

}


void spDBMForConfig::WriteSchemaTreeToDB(CBaseTree *root)
{
    MYSQL *conn = ConnectToDB();

    m_ctr = getLastUsedId(CConstants::SCHEMA_TBL);


    WriteSchemaTreeToDB(root, conn);

    DisconnectDB(conn);
}

void spDBMForConfig::WriteSchemaTreeToDB(CBaseTree *root, MYSQL *conn)
{
    
    if(root == NULL)
        return;
    else 
    {
        if(root->getParent() != NULL)
        {
            if(root->getNodeid() == 0)
                root->setNodeid(m_ctr);

            ostringstream querystream;
            querystream << "Insert into " << CConstants::SCHEMA_TBL <<" values (" <<
                        root->getNodeid() << "," <<
                        "\"" << root->getValue() << "\"" << "," <<
                        root->getParent()->getNodeid() << "," <<
                        "NULL" << ");";
            string query = querystream.str();
            
            if(mysql_real_query(conn, query.c_str(), query.size()))
                cout << "Error executing query " << query << mysql_error(conn);
        }
        
        map< string, vector< CBaseTree* > > mchilds = root->getChildren();

        for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
        {
            vector<CBaseTree*> vchilds = (*mit).second;
            for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
            {
                m_ctr++;
                WriteSchemaTreeToDB(*it, conn);
            }
        }

    }
}

void spDBMForConfig::WriteValueTreeToDB(CBaseTree *root)
{
    MYSQL *conn = ConnectToDB();

    m_ctr = getLastUsedId(CConstants::VALUE_TBL);

    m_ctr++;

    queue<CBaseTree*> nodeQueue;
    nodeQueue.push(root);

    string query;
    int count = 0;

    while( !nodeQueue.empty() )
    {
        CBaseTree* node = nodeQueue.front();
        nodeQueue.pop();

        if(node == NULL)
            continue;
        else
        {
            if(node->getParent() != NULL)
            {
                if(node->getNodeid() == 0)
                    node->setNodeid(m_ctr);

                m_ctr++;

                if(node->getNodeStatus() == 'A')
                {

                    if(count == 0)
                        query = "Insert into " + CConstants::VALUE_TBL + " values ";

                    ostringstream querystream;
                    querystream << "( " << node->getNodeid() << "," <<
                            "\"" << node->getValue() << "\"" << "," <<
                            node->getParent()->getNodeid() << "," <<
                            node->getSchemaLink()->getNodeid() << "," <<
                            "NULL" << ")";

                    count++;
                    query += querystream.str();

                    if(count == 100)
                    {
                        query += ";";
                        if(mysql_real_query(conn, query.c_str(), query.size()))
                            cout << "Error executing insert query " << query << mysql_error(conn);

                        count = 0;
                    }
                    else
                    {
                        query += ",";
                    }
                }
                else if(node->getNodeStatus() == 'D')
                {
                    ostringstream deletequeryStream;
                    deletequeryStream << "Delete from " <<
                            CConstants::VALUE_TBL <<
                            " where NodeID = " << node->getNodeid();
                    string deleteQuery = deletequeryStream.str();

                    if(mysql_real_query(conn, deleteQuery.c_str(), deleteQuery.size()))
                            cout << "Error executing delete query " << deleteQuery << mysql_error(conn);
                }
            }

            map< string, vector< CBaseTree* > > mchilds = node->getChildren();

            for(map< string, vector< CBaseTree* > >::iterator mit = mchilds.begin(); mit != mchilds.end(); mit++)
            {
                vector<CBaseTree*> vchilds = (*mit).second;
                for(vector<CBaseTree*>::iterator it = vchilds.begin(); it != vchilds.end(); it++)
                    nodeQueue.push(*it);
                
            }
        }
    }

    //Write the rest of the values
    if(count > 0)
    {
        if(query.at(query.size() - 1 ) == ',')
            query[query.size() - 1] = ';';
        
        if(mysql_real_query(conn, query.c_str(), query.size()))
            cout << "Error executing query " << query << mysql_error(conn);
    }
    
    DisconnectDB(conn);
}



void spDBMForConfig::WriteValueTreeToDB(CBaseTree *root, MYSQL *conn)
{
    
}

CBaseTree* spDBMForConfig::ReadSchemaTreeFromDB()
{
    MYSQL *conn = ConnectToDB();

    string query = "select * from " + CConstants::SCHEMA_TBL + " ORDER BY ParentNodeID ASC";

    if(mysql_real_query(conn, query.c_str(), query.size()))
    {
        cout << "Error executing query " << query;
        return NULL;
    }

    MYSQL_RES* result = mysql_store_result(conn);

    MYSQL_ROW row;
    CBaseTree *root = new CBaseTree("I am command root", NULL);
    
    while ((row = mysql_fetch_row(result)))
    {
        int nodeId = atoi(row[0]);
        string nodeValue = row[1];
        int parentid = atoi(row[2]);

        CBaseTree *parent = CBaseTreeManager::getNodeById(root, parentid);

        if(parent == NULL)
        {
            cout << "No parent found, ignore this node";
            continue;
        }
        else
        {
            parent->addChild(nodeValue, nodeValue, nodeId);
        }


    }

    DisconnectDB(conn);

    return root;

}

CBaseTree* spDBMForConfig::ReadValueTreeFromDB(CBaseTree *commandRoot)
{
    MYSQL *conn = ConnectToDB();

    string query = "select * from " + CConstants::VALUE_TBL + " ORDER BY ParentNodeID ASC";

    if(mysql_real_query(conn, query.c_str(), query.size()))
    {
        cout << "Error executing query " << query;
        return NULL;
    }

    MYSQL_RES* result = mysql_store_result(conn);

    MYSQL_ROW row;
    CBaseTree *root = new CBaseTree("I am value root", NULL);

    while ((row = mysql_fetch_row(result)))
    {
        int nodeId = atoi(row[0]);
        string nodeValue = row[1];
        int parentid = atoi(row[2]);
        int sectionId = atoi(row[3]);

        CBaseTree *parent = CBaseTreeManager::getNodeById(root, parentid);
        CBaseTree *commandNode = CBaseTreeManager::getNodeById(commandRoot, sectionId);

        if(parent == NULL)
        {
            cout << "No parent found, ignore this node";
            continue;
        }
        else
        {
            parent->addChild(nodeValue, commandNode->getValue(), nodeId, commandNode);
        }


    }

    DisconnectDB(conn);

    return root;

}

MYSQL* spDBMForConfig::ConnectToDB()
{
    MYSQL *conn;
    conn = mysql_init(NULL);

    if (mysql_real_connect(conn, "localhost", m_userName.c_str(), NULL, m_DBName.c_str(), 0, NULL, 0) != 0)
    {
    }
    else
        cout << "Unable to connect to DB" << mysql_error(conn);

    return conn;
}

void spDBMForConfig::DisconnectDB(MYSQL *conn)
{
    mysql_close(conn);
}

