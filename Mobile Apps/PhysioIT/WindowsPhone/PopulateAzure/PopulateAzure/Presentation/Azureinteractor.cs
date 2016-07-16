using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.WindowsAzure.MobileServices;
using Newtonsoft.Json;
using PopulateAzure.Common;
using CommonBackstage;

namespace PopulateAzure.Presentation
{
    class Azureinteractor
    {

        private TableInteractor _tb;

        public Azureinteractor()
        {
            _tb = new TableInteractor();
        }

        public async Task<bool> InsertIntoAzureTableFirstTime()
        {
            List<BodyPartDB> lbdb = _tb.PopulateTableFromXML<BodyPartDB>();
            List<ReasonsDB> lrdb = _tb.PopulateTableFromXML<ReasonsDB>();
            List<RemedyDB> lrmdb = _tb.PopulateTableFromXML<RemedyDB>();
            List<PostureDB> lpdb = _tb.PopulateTableFromXML<PostureDB>();
            List<ExcerciseDB> ledb = _tb.PopulateTableFromXML<ExcerciseDB>();
            DeviceDB ddb = _tb.PopulateDeviceDB();

            await InsertAzureTable<BodyPartDB>(lbdb);

            
            await InsertAzureTable<ReasonsDB>(lrdb);
            
            await InsertAzureTable<RemedyDB>(lrmdb);
            
            await InsertAzureTable<PostureDB>(lpdb);
            
            await InsertAzureTable<ExcerciseDB>(ledb);

            await InsertAzureTable<DeviceDB>(ddb);
            
            return true;

        }

        public async Task<bool> InsertAzureTable<T>(T tableName)
        {
            try
            {
                IMobileServiceTable<T> table = App.MobileService.GetTable<T>();
                await table.InsertAsync(tableName);
            }
            catch (Exception ex)
            {
                throw ex;
            }

            return true;
        }

        public async Task<bool> InsertAzureTable<T>(List<T> tableNames)
        {
            foreach (T tableName in tableNames)
            {
                await InsertAzureTable<T>(tableName);
            }

            return true;
        }

        public async Task<bool> UpdateAzureTable<T>(T tableName)
        {
            IMobileServiceTable<T> table = App.MobileService.GetTable<T>();
            await table.UpdateAsync(tableName);
            return true;
        }

        public async Task<List<T>> GetRecordsFromAzureTable<T>()
        {
            IMobileServiceTable<T> table = App.MobileService.GetTable<T>();
            return await table.ToListAsync();
        }

        
    }

}
