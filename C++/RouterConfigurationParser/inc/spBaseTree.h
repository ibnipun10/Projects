/* 
 * File:   spBaseTree.h
 * Author: ccr
 *
 * Created on February 9, 2015, 1:50 PM
 */

#ifndef SPBASETREE_H
#define	SPBASETREE_H

#include <string>
#include <vector>
#include <map>

using namespace std;

class CBaseTree
{
protected:
    string m_value;
    CBaseTree *m_parent;
    CBaseTree *m_link;
    map< string, vector< CBaseTree* > > m_childrenMap;
    int m_nodeId;
    
    /*
     * 'D' : Delete
     * 'A' : Add
     * 'R' : Read
     */
    char m_status;

public:
    CBaseTree(string, CBaseTree*);
    CBaseTree(string, int, CBaseTree*, char);
    ~CBaseTree();
    CBaseTree* addChild(string, string, int = 0, CBaseTree* = NULL, char = 'A');
    CBaseTree* getParent(){ return m_parent; }
    string getValue() { return m_value;}
    CBaseTree* hasChild(string);
    map< string, vector< CBaseTree* > > getChildren() { return m_childrenMap; }
    int getNodeid() { return m_nodeId; }
    void setNodeid(int nodeid) { m_nodeId = nodeid; }
    CBaseTree* getSchemaLink() { return m_link; }
    CBaseTree* hasChild(string, string);
    char getNodeStatus() { return m_status; }
    void setNodeStatus( char status) { m_status = status; }
};

#endif	/* SPBASETREE_H */

