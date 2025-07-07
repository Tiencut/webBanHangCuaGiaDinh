import { ref, reactive } from 'vue';
import api from '@/services/api';
import { 
  parseImportError, 
  validateImportFile, 
  createImportFormData, 
  downloadBlob,
  getImportConfig 
} from '@/utils/importHelpers';

/**
 * Composable for handling import operations
 * @param {string} importType - Type of import (suppliers, products, customers, orders)
 * @returns {Object} Import state and methods
 */
export function useImport(importType = 'suppliers') {
  const config = getImportConfig(importType);
  
  // Reactive state
  const importState = reactive({
    status: 'idle', // 'idle', 'uploading', 'completed', 'error'
    results: null,
    selectedFile: null,
    updateExisting: false
  });
  
  // Methods
  const validateFile = (file, options = {}) => {
    const defaultOptions = {
      allowedTypes: config.acceptedFileTypes ? config.acceptedFileTypes.split(',') : ['.csv'],
      maxSize: 10 * 1024 * 1024, // 10MB
      maxSizeMB: 10
    };
    
    return validateImportFile(file, { ...defaultOptions, ...options });
  };
  
  const setFile = (file) => {
    const validation = validateFile(file);
    if (validation.valid) {
      importState.selectedFile = file;
      return { success: true };
    } else {
      return { success: false, message: validation.message };
    }
  };
  
  const clearFile = () => {
    importState.selectedFile = null;
  };
  
  const executeImport = async (options = {}) => {
    if (!importState.selectedFile) {
      throw new Error('Vui lòng chọn file để import');
    }
    
    try {
      importState.status = 'uploading';
      importState.results = null;
      
      console.log(`Starting ${importType} import...`, {
        fileName: importState.selectedFile.name,
        fileSize: importState.selectedFile.size,
        updateExisting: importState.updateExisting,
        ...options
      });
      
      const formData = createImportFormData(importState.selectedFile, {
        updateExisting: importState.updateExisting,
        ...options
      });
      
      const response = await api.post(config.apiEndpoint, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      console.log(`${importType} import response:`, response.data);
      
      importState.status = 'completed';
      importState.results = response.data;
      
      return {
        success: true,
        data: response.data
      };
      
    } catch (error) {
      console.error(`${importType} import error:`, error);
      importState.status = 'error';
      importState.results = parseImportError(error);
      
      return {
        success: false,
        error: importState.results
      };
    }
  };
  
  const downloadTemplate = async () => {
    try {
      const response = await api.get(config.templateEndpoint, {
        responseType: 'blob'
      });
      
      const blob = new Blob([response.data], { 
        type: response.headers['content-type'] || 'text/csv' 
      });
      
      downloadBlob(blob, config.templateFilename);
      
      return { success: true };
    } catch (error) {
      console.error(`Error downloading ${importType} template:`, error);
      return { 
        success: false, 
        message: 'Có lỗi xảy ra khi tải template' 
      };
    }
  };
  
  const resetImport = () => {
    importState.status = 'idle';
    importState.results = null;
    importState.selectedFile = null;
    importState.updateExisting = false;
  };
  
  const getSuccessCount = () => {
    if (!importState.results || !importState.results.summary) return 0;
    return importState.results.summary.successCount || 
           importState.results.summary.importedRows || 0;
  };
  
  const hasErrors = () => {
    return importState.results && !importState.results.success;
  };
  
  const hasWarnings = () => {
    return importState.results && 
           importState.results.warnings && 
           importState.results.warnings.length > 0;
  };
  
  return {
    // State
    importState,
    config,
    
    // Methods
    validateFile,
    setFile,
    clearFile,
    executeImport,
    downloadTemplate,
    resetImport,
    
    // Computed helpers
    getSuccessCount,
    hasErrors,
    hasWarnings
  };
}
