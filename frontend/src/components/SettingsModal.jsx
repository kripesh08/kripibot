import React, { useState } from 'react';
import { X, Cpu, Sparkles, Sliders } from 'lucide-react';

export default function SettingsModal({
  isOpen,
  onClose,
  model,
  setModel,
  systemPrompt,
  setSystemPrompt,
}) {
  if (!isOpen) return null;

  const [tempModel, setTempModel] = useState(
    model && !model.includes('1.5') && !model.includes('2.5') ? model : 'gemini-3.6-flash'
  );
  const [tempPrompt, setTempPrompt] = useState(
    systemPrompt || 'You are KripiBot, a brilliant, friendly, and helpful AI assistant.'
  );

  const handleSave = () => {
    setModel(tempModel);
    localStorage.setItem('kripibot_gemini_model', tempModel);

    setSystemPrompt(tempPrompt.trim());
    localStorage.setItem('kripibot_system_prompt', tempPrompt.trim());

    onClose();
  };

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-card" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Sparkles size={20} style={{ color: 'var(--accent-primary)' }} />
            <span className="modal-title">AI Assistant Preferences</span>
          </div>
          <button className="icon-btn" onClick={onClose}>
            <X size={18} />
          </button>
        </div>

        <div className="modal-body">
          <div className="form-group">
            <label className="form-label" style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Cpu size={14} /> Gemini Model
            </label>
            <select
              className="form-select"
              value={tempModel}
              onChange={(e) => setTempModel(e.target.value)}
            >
              <option value="gemini-3.6-flash">Gemini 3.6 Flash (Recommended - Fastest & Latest)</option>
              <option value="gemini-3.7-flash">Gemini 3.7 Flash (Next-Gen Reasoning)</option>
              <option value="gemini-flash-latest">Gemini Flash Latest</option>
              <option value="gemini-pro-latest">Gemini Pro Latest</option>
            </select>
            <small style={{ color: 'var(--text-muted)', fontSize: '0.75rem' }}>
              Powered by your secure server-side Gemini configuration.
            </small>
          </div>

          <div className="form-group">
            <label className="form-label" style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
              <Sliders size={14} /> System Persona & Instructions
            </label>
            <textarea
              className="form-textarea"
              rows={4}
              placeholder="Define how KripiBot should behave (e.g., 'You are a senior Java architect')..."
              value={tempPrompt}
              onChange={(e) => setTempPrompt(e.target.value)}
            />
          </div>
        </div>

        <div className="modal-footer">
          <button className="btn-secondary" onClick={onClose}>
            Cancel
          </button>
          <button className="btn-primary" onClick={handleSave}>
            Save Preferences
          </button>
        </div>
      </div>
    </div>
  );
}
