package br.com.geomottu.api.model.entities;

import br.com.geomottu.api.dto.usuario.UsuarioDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "T_GEOMOTTU_USUARIO")
@SequenceGenerator(name = "user", sequenceName = "SQ_T_GEOMOTTU_USUARIO", allocationSize = 1)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @Column(name = "id_usuario")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user")
    private Long id;

    @Column(name = "nm_usuario", unique = true, nullable = false, length = 25)
    private String nome;

    @Column(name = "ds_senha", nullable = false, length = 100)
    private String senha;

    @Column(name = "tp_perfil", nullable = false, precision = 1)
    private Integer tipoPerfil;

    @ManyToOne
    @JoinColumn(name = "id_filial")
    private Filial filial;

    public Usuario(UsuarioDto json, Filial filial) {
        this.nome = json.nome();
        this.senha = json.senha();
        this.tipoPerfil = json.tipoPerfil();
        this.filial = filial;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.tipoPerfil == 1) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.nome;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
