import React, { useState } from 'react';
import {
  MessageSquare,
  Plus,
  Trash2,
  Edit2,
  Check,
  X,
  Settings,
  Search,
  Bot,
  PanelLeftClose,
  PanelLeftOpen,
} from 'lucide-react';

export default function Sidebar({
  conversations,
  currentConvId,
  onSelectConversation,
  onNewChat,
  onDeleteConversation,
  onRenameConversation,
  onOpenSettings,
  isCollapsed,
  onToggleCollapse,
}) {
  const [searchTerm, setSearchTerm] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editTitle, setEditTitle] = useState('');

  const filteredConversations = conversations.filter((c) =>
    c.title.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const startEditing = (e, conv) => {
    e.stopPropagation();
    setEditingId(conv.id);
    setEditTitle(conv.title);
  };

  const cancelEditing = (e) => {
    e.stopPropagation();
    setEditingId(null);
    setEditTitle('');
  };

  const saveEditing = (e, id) => {
    e.stopPropagation();
    if (editTitle.trim()) {
      onRenameConversation(id, editTitle.trim());
    }
    setEditingId(null);
  };

  return (
    <aside className={`sidebar ${isCollapsed ? 'collapsed' : ''}`}>
      <div className="sidebar-header">
        <div className="brand-logo">
          <div className="brand-icon">
            <Bot size={18} />
          </div>
          <span>KripiBot</span>
        </div>
        <button
          className="icon-btn"
          onClick={onToggleCollapse}
          title="Toggle Sidebar"
        >
          <PanelLeftClose size={18} />
        </button>
      </div>

      <button className="new-chat-btn" onClick={onNewChat}>
        <Plus size={18} />
        <span>New Chat</span>
      </button>

      <div className="sidebar-search">
        <div className="search-input-wrapper">
          <Search size={14} />
          <input
            type="text"
            className="search-input"
            placeholder="Search conversations..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      <div className="conversation-list">
        {filteredConversations.length === 0 ? (
          <div style={{ textAlign: 'center', padding: '24px 10px', color: 'var(--text-muted)', fontSize: '0.8rem' }}>
            {searchTerm ? 'No matching chats' : 'No conversations yet'}
          </div>
        ) : (
          filteredConversations.map((conv) => {
            const isActive = currentConvId === conv.id;
            const isEditing = editingId === conv.id;

            return (
              <div
                key={conv.id}
                className={`conversation-item ${isActive ? 'active' : ''}`}
                onClick={() => onSelectConversation(conv.id)}
              >
                <div className="conv-info">
                  <MessageSquare size={16} style={{ flexShrink: 0 }} />
                  {isEditing ? (
                    <input
                      type="text"
                      className="form-input"
                      style={{ padding: '2px 6px', fontSize: '0.85rem', width: '100%' }}
                      value={editTitle}
                      autoFocus
                      onClick={(e) => e.stopPropagation()}
                      onChange={(e) => setEditTitle(e.target.value)}
                      onKeyDown={(e) => {
                        if (e.key === 'Enter') saveEditing(e, conv.id);
                        if (e.key === 'Escape') cancelEditing(e);
                      }}
                    />
                  ) : (
                    <span className="conv-title" title={conv.title}>
                      {conv.title}
                    </span>
                  )}
                </div>

                <div className="conv-actions">
                  {isEditing ? (
                    <>
                      <button
                        className="icon-btn"
                        onClick={(e) => saveEditing(e, conv.id)}
                        title="Save"
                      >
                        <Check size={14} />
                      </button>
                      <button
                        className="icon-btn"
                        onClick={cancelEditing}
                        title="Cancel"
                      >
                        <X size={14} />
                      </button>
                    </>
                  ) : (
                    <>
                      <button
                        className="icon-btn"
                        onClick={(e) => startEditing(e, conv)}
                        title="Rename"
                      >
                        <Edit2 size={13} />
                      </button>
                      <button
                        className="icon-btn delete"
                        onClick={(e) => {
                          e.stopPropagation();
                          if (window.confirm(`Delete "${conv.title}"?`)) {
                            onDeleteConversation(conv.id);
                          }
                        }}
                        title="Delete"
                      >
                        <Trash2 size={13} />
                      </button>
                    </>
                  )}
                </div>
              </div>
            );
          })
        )}
      </div>

      <div className="sidebar-footer">
        <button className="footer-btn" onClick={onOpenSettings}>
          <Settings size={18} />
          <span>Settings & Persona</span>
        </button>
      </div>
    </aside>
  );
}
